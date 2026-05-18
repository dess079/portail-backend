package ca.sd.portail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sd.portail.entity.ThemeTranslation;

/**
 * Repository pour la gestion des traductions de thèmes
 */
@Repository
public interface ThemeTranslationRepository extends JpaRepository<ThemeTranslation, Long> {

    /**
     * Recherche les traductions pour un thème donné
     */
    List<ThemeTranslation> findByThemeId(Long themeId);
}