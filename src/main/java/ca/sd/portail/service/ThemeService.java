package ca.sd.portail.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.dto.ThemeDto;
import ca.sd.portail.dto.ThemeTranslationDto;
import ca.sd.portail.entity.Theme;
import ca.sd.portail.repository.ThemeRepository;

/**
 * Service métier pour la gestion des thèmes UI
 */
@Service
@Transactional(readOnly = true)
public class ThemeService {

    private static final Logger log = LoggerFactory.getLogger(ThemeService.class);

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    /**
     * Récupère tous les thèmes actifs
     */
    public List<ThemeDto> getAllActiveThemes() {
        log.debug("Récupération de tous les thèmes actifs");
        List<Theme> themes = themeRepository.findActiveThemesWithDetails("ACTIVE");
        return themes.stream()
                .map(theme -> mapToDto(theme))
                .collect(Collectors.toList());
    }

    /**
     * Récupère un thème spécifique par code
     */
    public Optional<ThemeDto> getThemeByCode(String code) {
        log.debug("Récupération du thème avec code: {}", code);
        return themeRepository.findByCode(code)
                .map(theme -> mapToDto(theme));
    }

    /**
     * Définit le thème actuel (pour l'instant, juste log - à implémenter avec session/user)
     */
    @Transactional
    public void setCurrentTheme(String themeCode) {
        log.info("Définition du thème actuel: {}", themeCode);
        // TODO: Implémenter la sauvegarde en DB ou session
        // Pour l'instant, juste validation que le thème existe
        Optional<Theme> theme = themeRepository.findByCode(themeCode);
        if (theme.isEmpty() || !"ACTIVE".equals(theme.get().getStatus())) {
            throw new IllegalArgumentException("Thème non trouvé ou inactif: " + themeCode);
        }
    }

    /**
     * Convertit une entité Theme en DTO
     */
    public ThemeDto mapToDto(Theme theme) {
        List<ThemeTranslationDto> translations = theme.getTranslations().stream()
                .map(t -> new ThemeTranslationDto(t.getLanguageCode(), t.getName()))
                .collect(Collectors.toList());

        return new ThemeDto(
                theme.getCode(),
                theme.getName(),
                theme.getThemeType().getName(),
                theme.getThemeDefinition(),
                translations
        );
    }
}