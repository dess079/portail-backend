package ca.sd.portail.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.sd.portail.dto.AgentPlanHistoryResponse;
import ca.sd.portail.dto.BulkDeleteRequest;
import ca.sd.portail.entity.AgentHit;
import ca.sd.portail.service.AgentPlanService;

import jakarta.validation.Valid;

/**
 * Contrôleur REST gérant les plans d'exécution d'agents IA :
 * historique paginé avec recherche, hits par plan et suppressions.
 */
@RestController
@RequestMapping("/api/v1/agent-activity")
public class AgentPlanController {

    private static final Logger log = LoggerFactory.getLogger(AgentPlanController.class);

    private final AgentPlanService agentPlanService;

    /** Injection par constructeur. */
    public AgentPlanController(AgentPlanService agentPlanService) {
        this.agentPlanService = agentPlanService;
    }

    /**
     * Retourne l'historique paginé des plans avec filtres optionnels et recherche globale.
     * GET /api/v1/agent-activity/plans?page=0&amp;size=20&amp;search=mot
     *
     * @param page      index de page (0-based)
     * @param size      taille de page
     * @param planId    filtre par identifiant de plan
     * @param objective filtre par objectif
     * @param status    filtre par statut
     * @param search    recherche libre sur planId ou objectif
     * @return historique paginé
     */
    @GetMapping("/plans")
    public ResponseEntity<AgentPlanHistoryResponse> getPlansHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String planId,
            @RequestParam(required = false) String objective,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(
            agentPlanService.getPlansHistory(page, size, planId, objective, status, search));
    }

    /**
     * Retourne tous les hits d'un plan donné.
     * GET /api/v1/agent-activity/plans/{planId}/hits
     *
     * @param planId identifiant du plan
     * @return liste des hits
     */
    @GetMapping("/plans/{planId}/hits")
    public ResponseEntity<List<AgentHit>> getPlanHits(@PathVariable String planId) {
        return ResponseEntity.ok(agentPlanService.getPlanHits(planId));
    }

    /**
     * Supprime un plan et tous ses hits.
     * DELETE /api/v1/agent-activity/plans/{planId}
     *
     * @param planId identifiant du plan
     */
    @DeleteMapping("/plans/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable String planId) {
        log.debug("DELETE /api/v1/agent-activity/plans/{}", planId);
        agentPlanService.deletePlan(planId);
    }

    /**
     * Supprime en masse plusieurs plans et leurs hits.
     * DELETE /api/v1/agent-activity/plans
     *
     * @param request corps contenant la liste des planIds
     */
    @DeleteMapping("/plans")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlans(@RequestBody @Valid BulkDeleteRequest request) {
        log.debug("DELETE /api/v1/agent-activity/plans — count={}", request.planIds().size());
        agentPlanService.deletePlans(request.planIds());
    }
}
