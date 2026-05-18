package ca.sd.portail.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.dto.AgentActivityLiveResponse;
import ca.sd.portail.dto.AgentActivityStatsResponse;
import ca.sd.portail.dto.AgentHitRequest;
import ca.sd.portail.entity.AgentHit;
import ca.sd.portail.entity.AgentPlan;
import ca.sd.portail.repository.AgentHitRepository;
import ca.sd.portail.repository.AgentHitRepository.AgentHitStats;
import ca.sd.portail.repository.AgentHitRepository.SkillHitStats;
import ca.sd.portail.repository.AgentPlanRepository;

/**
 * Service gérant l'activité des agents IA : enregistrement des hits,
 * vue live et statistiques agrégées.
 */
@Service
public class AgentActivityService {

    private static final Logger log = LoggerFactory.getLogger(AgentActivityService.class);

    private final AgentHitRepository agentHitRepository;
    private final AgentPlanRepository agentPlanRepository;

    public AgentActivityService(AgentHitRepository agentHitRepository,
                                AgentPlanRepository agentPlanRepository) {
        this.agentHitRepository = agentHitRepository;
        this.agentPlanRepository = agentPlanRepository;
    }

    /**
     * Enregistre un hit d'activité.
     * Crée le plan s'il n'existe pas encore (upsert), puis insère le hit.
     * Met à jour completedTasks si le statut est 'completed'.
     */
    @Transactional
    public AgentHit recordHit(AgentHitRequest request) {
        log.debug("Recording agent hit: agent={}, plan={}, status={}", 
                  request.getAgentName(), request.getPlanId(), request.getStatus());

        if (request.getPlanId() != null) {
            agentPlanRepository.findById(request.getPlanId()).ifPresentOrElse(
                plan -> {
                    if ("completed".equals(request.getStatus())) {
                        plan.setCompletedTasks(
                            plan.getCompletedTasks() == null ? 1 : plan.getCompletedTasks() + 1);
                    }
                    if (request.getPlanStatus() != null) {
                        plan.setStatus(request.getPlanStatus());
                        if ("completed".equals(request.getPlanStatus()) && plan.getCompletedAt() == null) {
                            plan.setCompletedAt(java.time.OffsetDateTime.now());
                        }
                    }
                    agentPlanRepository.save(plan);
                },
                () -> {
                    String objective = request.getPlanObjective() != null
                        ? request.getPlanObjective()
                        : "Plan " + request.getPlanId();
                    AgentPlan newPlan = new AgentPlan(request.getPlanId(), objective);
                    if ("completed".equals(request.getStatus())) {
                        newPlan.setCompletedTasks(1);
                    }
                    agentPlanRepository.save(newPlan);
                    log.info("Created new plan: {}", request.getPlanId());
                }
            );
        }

        AgentHit hit = new AgentHit();
        hit.setPlanId(request.getPlanId());
        hit.setTaskId(request.getTaskId());
        hit.setAgentName(request.getAgentName());
        hit.setSkillName(request.getSkillName());
        hit.setStatus(request.getStatus());
        hit.setDescription(request.getDescription());
        hit.setWave(request.getWave());
        hit.setDurationSeconds(request.getDurationSeconds());

        AgentHit saved = agentHitRepository.save(hit);
        log.debug("Saved agent hit id={}", saved.getId());
        return saved;
    }

    /**
     * Retourne la vue live : 50 derniers hits + informations sur le plan actif.
     */
    public AgentActivityLiveResponse getLiveActivity() {
        List<AgentHit> recentHits = agentHitRepository.findTop50ByOrderByCreatedAtDesc();

        String activePlanId = null;
        String activePlanObjective = null;

        // Plan actif = premier plan "running" trouvé parmi les hits récents
        for (AgentHit hit : recentHits) {
            if (hit.getPlanId() != null) {
                AgentPlan plan = agentPlanRepository.findById(hit.getPlanId()).orElse(null);
                if (plan != null && "running".equals(plan.getStatus())) {
                    activePlanId = plan.getPlanId();
                    activePlanObjective = plan.getObjective();
                    break;
                }
            }
        }

        int activeAgentsCount = (int) recentHits.stream()
            .map(AgentHit::getAgentName)
            .filter(name -> name != null)
            .distinct()
            .count();

        return new AgentActivityLiveResponse(recentHits, activePlanId, activePlanObjective, activeAgentsCount);
    }

    /**
     * Retourne les statistiques agrégées : top agents, top skills, totaux.
     */
    public AgentActivityStatsResponse getStats() {
        List<AgentHitStats> topAgents = agentHitRepository.countByAgentGrouped();
        List<SkillHitStats> topSkills = agentHitRepository.countBySkillGrouped();
        long totalHits = agentHitRepository.count();
        long totalPlans = agentPlanRepository.count();

        return new AgentActivityStatsResponse(topAgents, topSkills, totalHits, totalPlans);
    }

}
