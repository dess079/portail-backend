package ca.sd.portail.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.sd.portail.dto.AppSettingDto;
import ca.sd.portail.service.AppSettingService;

/**
 * Contrôleur REST pour la gestion des paramètres applicatifs (CRUD clé-valeur)
 * Zéro logique métier — délègue tout au service
 */
@RestController
@RequestMapping("/api/settings")
public class AppSettingController {

    private final AppSettingService appSettingService;

    public AppSettingController(AppSettingService appSettingService) {
        this.appSettingService = appSettingService;
    }

    /**
     * Récupère tous les paramètres d'un utilisateur
     */
    @GetMapping
    public ResponseEntity<List<AppSettingDto>> getAllSettings(@RequestParam String userId) {
        return ResponseEntity.ok(appSettingService.getAllSettings(userId));
    }

    /**
     * Récupère un paramètre spécifique par clé
     */
    @GetMapping("/{key}")
    public ResponseEntity<AppSettingDto> getSetting(
            @PathVariable String key,
            @RequestParam String userId) {
        return appSettingService.getSetting(userId, key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée ou met à jour un paramètre
     */
    @PutMapping
    public ResponseEntity<AppSettingDto> saveSetting(@RequestBody AppSettingDto dto) {
        AppSettingDto saved = appSettingService.saveSetting(
                dto.getUserId(), dto.getKey(), dto.getValue(), dto.getType(), dto.getDescription());
        return ResponseEntity.ok(saved);
    }

    /**
     * Supprime un paramètre spécifique
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteSetting(
            @PathVariable String key,
            @RequestParam String userId) {
        appSettingService.deleteSetting(userId, key);
        return ResponseEntity.noContent().build();
    }
}
