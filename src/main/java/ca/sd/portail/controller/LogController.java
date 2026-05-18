package ca.sd.portail.controller;

import ca.sd.portail.dto.LogEntryDto;
import ca.sd.portail.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des logs
 * Expose les APIs pour la consultation des logs d'applications
 */
@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Récupère les logs pour une application spécifique
     * 
     * @param applicationCode Le code de l'application
     * @param limit Nombre maximum de lignes à retourner (optionnel)
     * @param level Niveau de log à filtrer (optionnel)
     * @param search Terme de recherche dans les messages (optionnel)
     * @return Liste des entrées de log
     */
    @GetMapping("/{applicationCode}")
    public ResponseEntity<List<LogEntryDto>> getLogsForApplication(
            @PathVariable String applicationCode,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "search", required = false) String search) {

        log.info("GET /api/v1/logs/{} - limit: {}, level: {}, search: {}", 
                applicationCode, limit, level, search);

        try {
            List<LogEntryDto> logs = logService.getLogsForApplication(applicationCode, limit, level, search);
            
            log.debug("Récupération de {} entrées de log pour l'application: {}", logs.size(), applicationCode);
            return ResponseEntity.ok(logs);

        } catch (Exception e) {
            log.error("Erreur lors de la récupération des logs pour l'application: {}", applicationCode, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les logs récents pour toutes les applications
     * 
     * @param limit Nombre maximum de lignes par application (optionnel, défaut: 50)
     * @param level Niveau de log à filtrer (optionnel)
     * @return Map des logs par application
     */
    @GetMapping("/recent")
    public ResponseEntity<RecentLogsResponse> getRecentLogs(
            @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit,
            @RequestParam(value = "level", required = false) String level) {

        log.info("GET /api/v1/logs/recent - limit: {}, level: {}", limit, level);

        try {
            // Applications connues du système
            String[] applications = {"portail", "lottosylvain", "lottosylvain", "sed-backend", "sed-frontend"};
            
            RecentLogsResponse response = new RecentLogsResponse();
            
            for (String appCode : applications) {
                List<LogEntryDto> appLogs = logService.getLogsForApplication(appCode, limit, level, null);
                if (!appLogs.isEmpty()) {
                    response.addApplicationLogs(appCode, appLogs);
                }
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erreur lors de la récupération des logs récents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les niveaux de log disponibles
     * 
     * @return Liste des niveaux de log supportés
     */
    @GetMapping("/levels")
    public ResponseEntity<List<String>> getLogLevels() {
        log.debug("GET /api/v1/logs/levels");

        try {
            List<String> levels = List.of("TRACE", "DEBUG", "INFO", "WARN", "ERROR");
            return ResponseEntity.ok(levels);

        } catch (Exception e) {
            log.error("Erreur lors de la récupération des niveaux de log", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Vérifie la disponibilité des logs pour une application
     * 
     * @param applicationCode Le code de l'application
     * @return Statut de disponibilité des logs
     */
    @GetMapping("/{applicationCode}/status")
    public ResponseEntity<LogStatusDto> getLogStatus(@PathVariable String applicationCode) {
        log.debug("GET /api/v1/logs/{}/status", applicationCode);

        try {
            // Tenter de récupérer quelques logs pour vérifier la disponibilité
            List<LogEntryDto> testLogs = logService.getLogsForApplication(applicationCode, 1, null, null);
            
            LogStatusDto status = new LogStatusDto(
                applicationCode,
                !testLogs.isEmpty(),
                testLogs.isEmpty() ? "Aucun log disponible" : "Logs disponibles"
            );

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            log.error("Erreur lors de la vérification du statut des logs pour: {}", applicationCode, e);
            
            LogStatusDto status = new LogStatusDto(
                applicationCode,
                false,
                "Erreur lors de la lecture des logs: " + e.getMessage()
            );
            
            return ResponseEntity.ok(status);
        }
    }

    /**
     * DTO pour la réponse des logs récents
     */
    public static class RecentLogsResponse {
        private final java.util.Map<String, List<LogEntryDto>> applicationLogs = new java.util.HashMap<>();

        public void addApplicationLogs(String applicationCode, List<LogEntryDto> logs) {
            this.applicationLogs.put(applicationCode, logs);
        }

        public java.util.Map<String, List<LogEntryDto>> getApplicationLogs() {
            return applicationLogs;
        }

        public int getTotalApplications() {
            return applicationLogs.size();
        }

        public int getTotalLogEntries() {
            return applicationLogs.values().stream()
                    .mapToInt(List::size)
                    .sum();
        }
    }

    /**
     * DTO pour le statut des logs d'une application
     */
    public static class LogStatusDto {
        private final String applicationCode;
        private final boolean available;
        private final String message;

        public LogStatusDto(String applicationCode, boolean available, String message) {
            this.applicationCode = applicationCode;
            this.available = available;
            this.message = message;
        }

        public String getApplicationCode() { return applicationCode; }
        public boolean isAvailable() { return available; }
        public String getMessage() { return message; }
    }
}