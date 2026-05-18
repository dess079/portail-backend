package ca.sd.portail.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.dto.AppSettingDto;
import ca.sd.portail.entity.AppSetting;
import ca.sd.portail.repository.AppSettingRepository;

/**
 * Service métier pour la gestion des paramètres applicatifs (CRUD clé-valeur)
 */
@Service
@Transactional(readOnly = true)
public class AppSettingService {

    private static final Logger log = LoggerFactory.getLogger(AppSettingService.class);

    private final AppSettingRepository appSettingRepository;

    public AppSettingService(AppSettingRepository appSettingRepository) {
        this.appSettingRepository = appSettingRepository;
    }

    /**
     * Récupère tous les paramètres d'un utilisateur
     */
    public List<AppSettingDto> getAllSettings(String userId) {
        log.info("LECTURE BD - Récupération de tous les paramètres pour userId: {}", userId);
        return appSettingRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Récupère un paramètre spécifique par clé
     */
    public Optional<AppSettingDto> getSetting(String userId, String key) {
        log.info("LECTURE BD - Récupération du paramètre '{}' pour userId: {}", key, userId);
        return appSettingRepository.findByUserIdAndSettingKey(userId, key)
                .map(this::mapToDto);
    }

    /**
     * Crée ou met à jour un paramètre (upsert)
     */
    @Transactional
    public AppSettingDto saveSetting(String userId, String key, String value, String type, String description) {
        log.info("ÉCRITURE BD - Sauvegarde du paramètre '{}' = '{}' pour userId: {}", key, value, userId);

        Optional<AppSetting> existing = appSettingRepository.findByUserIdAndSettingKey(userId, key);
        AppSetting setting;

        if (existing.isPresent()) {
            setting = existing.get();
            setting.setSettingValue(value);
            if (type != null) setting.setSettingType(type);
            if (description != null) setting.setDescription(description);
        } else {
            setting = new AppSetting(userId, key, value,
                    type != null ? type : "STRING",
                    description);
        }

        AppSetting saved = appSettingRepository.save(setting);
        log.info("ÉCRITURE BD - Paramètre sauvegardé: '{}' = '{}' pour userId: {}", key, value, userId);
        return mapToDto(saved);
    }

    /**
     * Supprime un paramètre spécifique
     */
    @Transactional
    public void deleteSetting(String userId, String key) {
        log.info("ÉCRITURE BD - Suppression du paramètre '{}' pour userId: {}", key, userId);
        appSettingRepository.deleteByUserIdAndSettingKey(userId, key);
    }

    private AppSettingDto mapToDto(AppSetting entity) {
        return new AppSettingDto(
                entity.getUserId(),
                entity.getSettingKey(),
                entity.getSettingValue(),
                entity.getSettingType(),
                entity.getDescription()
        );
    }
}
