package ca.sd.portail.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sd.portail.dto.ThemeDto;
import ca.sd.portail.dto.ThemeRequestDto;
import ca.sd.portail.service.ThemeService;

/**
 * Contrôleur REST pour la gestion des thèmes UI
 * Expose les APIs pour récupérer les thèmes et leurs définitions
 */
@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private static final Logger log = LoggerFactory.getLogger(ThemeController.class);

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Récupère tous les thèmes actifs
     *
     * @return Liste des thèmes actifs
     */
    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        log.info("GET /api/themes - Récupération de tous les thèmes actifs");

        try {
            List<ThemeDto> themes = themeService.getAllActiveThemes();
            return ResponseEntity.ok(themes);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des thèmes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Définit le thème actuel pour l'utilisateur
     *
     * @param request Objet contenant le code du thème
     * @return Confirmation de la mise à jour
     */
    @PostMapping("/current")
    public ResponseEntity<Void> setCurrentTheme(@RequestBody ThemeRequestDto request) {
        log.info("API POST /api/themes/current - DÉBUT - Thème demandé: {} (NOTE: Cette API ne persiste pas en BD - utiliser /api/preferences)", request.getTheme());

        try {
            themeService.setCurrentTheme(request.getTheme());
            log.info("API POST /api/themes/current - SUCCÈS - Thème validé: {} (mais non persisté)", request.getTheme());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException iae) {
            // Invalid theme requested (not found or inactive) -> return 400 Bad Request
            log.warn("API POST /api/themes/current - MAUVAIS ARGUMENT - Thème: {} - Message: {}", request.getTheme(), iae.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("API POST /api/themes/current - ERREUR - Thème: {} - Message: {}", request.getTheme(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}