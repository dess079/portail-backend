package ca.sd.portail.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sd.portail.dto.AgentActivityLiveResponse;
import ca.sd.portail.dto.AgentActivityStatsResponse;
import ca.sd.portail.dto.AgentHitRequest;
import ca.sd.portail.entity.AgentHit;
import ca.sd.portail.service.AgentActivityService;

/**
 * Contrôleur REST pour le monitoring en temps réel de l'activité des agents IA.
 * Expose les endpoints d'enregistrement de hits, vue live et statistiques.
 */
@RestController
@RequestMapping("/api/v1/agent-activity")
public class AgentActivityController {

    private static final Logger log = LoggerFactory.getLogger(AgentActivityController.class);

    private final AgentActivityService agentActivityService;

    /** Injection par constructeur. */
    public AgentActivityController(AgentActivityService agentActivityService) {
        this.agentActivityService = agentActivityService;
    }

    /**
     * Enregistre un hit d'activité d'agent.
     * POST /api/v1/agent-activity/hit
     *
     * @param request données du hit
     * @return hit enregistré
     */
    @PostMapping("/hit")
    public ResponseEntity<AgentHit> recordHit(@RequestBody AgentHitRequest request) {
        log.debug("POST /api/v1/agent-activity/hit — agent={}", request.getAgentName());
        return ResponseEntity.ok(agentActivityService.recordHit(request));
    }

    /**
     * Retourne la vue live : 50 derniers hits et plan actif.
     * GET /api/v1/agent-activity/live
     *
     * @return vue live
     */
    @GetMapping("/live")
    public ResponseEntity<AgentActivityLiveResponse> getLiveActivity() {
        return ResponseEntity.ok(agentActivityService.getLiveActivity());
    }

    /**
     * Retourne les statistiques agrégées : top agents, top skills, totaux.
     * GET /api/v1/agent-activity/stats
     *
     * @return statistiques
     */
    @GetMapping("/stats")
    public ResponseEntity<AgentActivityStatsResponse> getStats() {
        return ResponseEntity.ok(agentActivityService.getStats());
    }
}

