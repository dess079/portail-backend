package ca.sd.portail.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ca.sd.portail.dto.AgentPlanHistoryResponse;
import ca.sd.portail.dto.AgentPlanResponse;
import ca.sd.portail.entity.AgentHit;
import ca.sd.portail.entity.AgentPlan;
import ca.sd.portail.repository.AgentHitRepository;
import ca.sd.portail.repository.AgentPlanRepository;

/**
 * Service gérant les opérations sur les plans d'exécution d'agents :
 * historique paginé avec recherche, consultation des hits et suppression.
 */
@Service
public class AgentPlanService {

    private static final Logger log = LoggerFactory.getLogger(AgentPlanService.class);

    private final AgentHitRepository agentHitRepository;
    private final AgentPlanRepository agentPlanRepository;

    /** Injection par constructeur. */
    public AgentPlanService(AgentHitRepository agentHitRepository,
                            AgentPlanRepository agentPlanRepository) {
        this.agentHitRepository = agentHitRepository;
        this.agentPlanRepository = agentPlanRepository;
    }

    /**
     * Retourne l'historique paginé des plans.
     * Filtres optionnels par colonne (planId, objective, status) ou par mot-clé global (search).
     *
     * @param page      index de page (0-based)
     * @param size      taille de page
     * @param planId    filtre LIKE sur l'identifiant du plan
     * @param objective filtre LIKE sur l'objectif
     * @param status    filtre exact sur le statut
     * @param search    recherche globale LIKE sur planId OU objective
     * @return page de résultats
     */
    public AgentPlanHistoryResponse getPlansHistory(int page, int size,
                                                    String planId, String objective,
                                                    String status, String search) {
        String s = emptyToNull(search);
        Page<AgentPlan> planPage;
        if (s != null) {
            planPage = agentPlanRepository.findAllWithSearch(s, PageRequest.of(page, size));
        } else {
            boolean hasFilter = planId != null || objective != null || status != null;
            planPage = hasFilter
                ? agentPlanRepository.findAllWithFilters(
                    emptyToNull(planId), emptyToNull(objective), emptyToNull(status),
                    PageRequest.of(page, size))
                : agentPlanRepository.findAllByOrderByStartedAtDesc(PageRequest.of(page, size));
        }

        List<AgentPlanResponse> content = planPage.getContent().stream()
            .map(plan -> AgentPlanResponse.from(plan,
                agentPlanRepository.countHitsByPlanId(plan.getPlanId())))
            .collect(Collectors.toList());

        return new AgentPlanHistoryResponse(page, size,
            planPage.getTotalElements(), planPage.getTotalPages(), content);
    }

    /**
     * Retourne tous les hits d'un plan, triés du plus récent au plus ancien.
     *
     * @param planId identifiant du plan
     * @return liste des hits
     */
    public List<AgentHit> getPlanHits(String planId) {
        return agentHitRepository.findByPlanIdOrderByCreatedAtDesc(planId);
    }

    /**
     * Supprime un plan et tous ses hits associés. Lève 404 si introuvable.
     *
     * @param planId identifiant du plan
     */
    @Transactional
    public void deletePlan(String planId) {
        if (!agentPlanRepository.existsById(planId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found: " + planId);
        }
        agentHitRepository.deleteByPlanId(planId);
        agentPlanRepository.deleteById(planId);
        log.info("Deleted plan and its hits: {}", planId);
    }

    /**
     * Supprime en masse plusieurs plans et tous leurs hits associés.
     *
     * @param planIds liste des identifiants à supprimer
     */
    @Transactional
    public void deletePlans(List<String> planIds) {
        agentHitRepository.deleteByPlanIdIn(planIds);
        agentPlanRepository.deleteByPlanIdIn(planIds);
        log.info("Bulk deleted {} plans", planIds.size());
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
