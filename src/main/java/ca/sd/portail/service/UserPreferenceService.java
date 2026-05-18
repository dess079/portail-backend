package ca.sd.portail.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.dto.UserPreferenceDto;
import ca.sd.portail.entity.UserPreference;
import ca.sd.portail.repository.UserPreferenceRepository;

/**
 * Service métier pour la gestion des préférences utilisateur
 */
@Service
@Transactional(readOnly = true)
public class UserPreferenceService {

    private static final Logger log = LoggerFactory.getLogger(UserPreferenceService.class);

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    /**
     * Récupère les préférences d'un utilisateur
     */
    public Optional<UserPreferenceDto> getUserPreferences(String userId) {
        log.info("LECTURE BD - Récupération des préférences utilisateur pour userId: {}", userId);
        Optional<UserPreferenceDto> result = userPreferenceRepository.findTopByUserIdOrderByIdDesc(userId)
                .map(this::mapToDto);
        if (result.isPresent()) {
            log.info("LECTURE BD - Préférences trouvées pour userId: {} - langue: {}, thème: {}", 
                    userId, result.get().getLanguage(), result.get().getTheme());
        } else {
            log.info("LECTURE BD - Aucune préférence trouvée pour userId: {} - utilisation des valeurs par défaut", userId);
        }
        return result;
    }

    /**
     * Sauvegarde ou met à jour les préférences d'un utilisateur
     */
    @Transactional
    public UserPreferenceDto saveUserPreferences(String userId, String language, String theme) {
        log.info("ÉCRITURE BD - Sauvegarde des préférences utilisateur pour userId: {} - langue: {}, thème: {}", userId, language, theme);

        Optional<UserPreference> existing = userPreferenceRepository.findTopByUserIdOrderByIdDesc(userId);
        UserPreference preference;

        if (existing.isPresent()) {
            log.info("ÉCRITURE BD - Mise à jour des préférences existantes pour userId: {}", userId);
            preference = existing.get();
            preference.setLanguage(language);
            preference.setTheme(theme);
        } else {
            log.info("ÉCRITURE BD - Création de nouvelles préférences pour userId: {}", userId);
            preference = new UserPreference(userId, language, theme);
        }

        UserPreference saved = userPreferenceRepository.save(preference);
        log.info("ÉCRITURE BD - Préférences sauvegardées avec succès pour userId: {} - langue: {}, thème: {}", 
                userId, saved.getLanguage(), saved.getTheme());
        return mapToDto(saved);
    }

    private UserPreferenceDto mapToDto(UserPreference entity) {
        return new UserPreferenceDto(entity.getUserId(), entity.getLanguage(), entity.getTheme());
    }
}