package ca.sd.portail.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.sd.portail.dto.UserPreferenceDto;
import ca.sd.portail.service.UserPreferenceService;

/**
 * Contrôleur REST pour la gestion des préférences utilisateur
 * Expose les APIs pour récupérer et sauvegarder les préférences (langue et thème)
 */
@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {

    private static final Logger log = LoggerFactory.getLogger(UserPreferenceController.class);

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * Récupère les préférences d'un utilisateur
     *
     * @param userId L'ID de l'utilisateur
     * @return Les préférences utilisateur ou valeurs par défaut
     */
    @GetMapping
    public ResponseEntity<UserPreferenceDto> getUserPreferences(@RequestParam String userId) {
        log.info("API GET /api/preferences - DÉBUT - Utilisateur: {}", userId);

        try {
            UserPreferenceDto preferences = userPreferenceService.getUserPreferences(userId)
                    .orElse(new UserPreferenceDto(userId, "fr", "default"));
            log.info("API GET /api/preferences - SUCCÈS - Utilisateur: {} - langue: {}, thème: {}", 
                    userId, preferences.getLanguage(), preferences.getTheme());
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            log.error("API GET /api/preferences - ERREUR - Utilisateur: {} - Message: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sauvegarde les préférences d'un utilisateur
     *
     * @param userId L'ID de l'utilisateur
     * @param language La langue préférée
     * @param theme Le thème préféré
     * @return Les préférences sauvegardées
     */
    @PutMapping
    public ResponseEntity<UserPreferenceDto> saveUserPreferences(
            @RequestParam String userId,
            @RequestParam String language,
            @RequestParam String theme) {
        log.info("API PUT /api/preferences - DÉBUT - Utilisateur: {} - langue: {}, thème: {}", userId, language, theme);

        try {
            UserPreferenceDto saved = userPreferenceService.saveUserPreferences(userId, language, theme);
            log.info("API PUT /api/preferences - SUCCÈS - Utilisateur: {} - langue: {}, thème: {}", 
                    userId, saved.getLanguage(), saved.getTheme());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("API PUT /api/preferences - ERREUR - Utilisateur: {} - langue: {}, thème: {} - Message: {}", 
                    userId, language, theme, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}