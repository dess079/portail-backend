package ca.sd.portail.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.sd.portail.dto.ApplicationDto;
import ca.sd.portail.dto.ApplicationSummaryDto;
import ca.sd.portail.service.ApplicationService;
import jakarta.validation.Valid;

/**
 * Contrôleur REST pour la gestion des applications
 * Expose les APIs pour les opérations CRUD sur les applications
 */
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Récupère toutes les applications ou celles avec un statut spécifique
     * 
     * @param status Statut optionnel pour filtrer les applications
     * @param environment Environnement détecté par le frontend (override la détection automatique)
     * @return Liste des applications
     */
    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getAllApplications(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "environment", required = false) String environment) {
        
        log.info("GET /api/v1/applications - status: {}, environment: {}", status, environment);
        
        try {
            List<ApplicationDto> applications;
            
            if (status != null && !status.trim().isEmpty()) {
                applications = applicationService.getApplicationsByStatus(status, environment);
            } else {
                applications = applicationService.getAllApplications(environment);
            }
            
            return ResponseEntity.ok(applications);
            
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des applications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les résumés des applications (code, nom et statut)
     * 
     * @param status Filtre optionnel par statut
     * @return Liste des résumés d'applications
     */
    @GetMapping("/summaries")
    public ResponseEntity<List<ApplicationSummaryDto>> getApplicationSummaries(
            @RequestParam(value = "status", required = false) String status) {
        log.info("GET /api/v1/applications/summaries - status: {}", status);
        
        try {
            List<ApplicationSummaryDto> summaries = applicationService.getApplicationSummaries(status);
            return ResponseEntity.ok(summaries);
            
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des résumés d'applications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crée une nouvelle application
     * 
     * @param applicationDto Les données de l'application à créer
     * @return L'application créée
     */
    @PostMapping
    public ResponseEntity<ApplicationDto> createApplication(@Valid @RequestBody ApplicationDto applicationDto) {
        log.info("POST /api/v1/applications - création de l'application: {}", applicationDto.getCode());
        
        try {
            ApplicationDto createdApplication = applicationService.createApplication(applicationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
            
        } catch (IllegalArgumentException e) {
            log.warn("Erreur de validation lors de la création de l'application: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
            
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'application", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour une application existante
     * 
     * @param code Le code de l'application à mettre à jour
     * @param applicationDto Les nouvelles données
     * @return L'application mise à jour
     */
    @PutMapping("/{code}")
    public ResponseEntity<ApplicationDto> updateApplication(
            @PathVariable String code, 
            @Valid @RequestBody ApplicationDto applicationDto) {
        
        log.info("PUT /api/v1/applications/{} - mise à jour de l'application", code);
        
        try {
            ApplicationDto updatedApplication = applicationService.updateApplication(code, applicationDto);
            return ResponseEntity.ok(updatedApplication);
            
        } catch (RuntimeException e) {
            log.warn("Application non trouvée pour la mise à jour: {}", e.getMessage());
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de l'application avec l'ID: {}", code, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Supprime une application
     * 
     * @param code Le code de l'application à supprimer
     * @return Réponse de suppression
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String code) {
        log.info("DELETE /api/v1/applications/{} - suppression de l'application", code);
        
        try {
            applicationService.deleteApplication(code);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            log.warn("Application non trouvée pour la suppression: {}", e.getMessage());
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'application avec le code: {}", code, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les statistiques des applications par statut
     * 
     * @return Map des statistiques
     */
    @GetMapping("/stats")
    public ResponseEntity<ApplicationStatsDto> getApplicationStats() {
        log.info("GET /api/v1/applications/stats");
        
        try {
            long activeCount = applicationService.countApplicationsByStatus("active");
            long inactiveCount = applicationService.countApplicationsByStatus("inactive");
            long errorCount = applicationService.countApplicationsByStatus("error");
            
            ApplicationStatsDto stats = new ApplicationStatsDto(activeCount, inactiveCount, errorCount);
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des statistiques", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DTO pour les statistiques des applications
     */
    public static class ApplicationStatsDto {
        private final long activeCount;
        private final long inactiveCount;
        private final long errorCount;

        public ApplicationStatsDto(long activeCount, long inactiveCount, long errorCount) {
            this.activeCount = activeCount;
            this.inactiveCount = inactiveCount;
            this.errorCount = errorCount;
        }

        public long getActiveCount() { return activeCount; }
        public long getInactiveCount() { return inactiveCount; }
        public long getErrorCount() { return errorCount; }
        public long getTotalCount() { return activeCount + inactiveCount + errorCount; }
    }
}