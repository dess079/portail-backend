package ca.sd.portail.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.sd.portail.entity.Theme;

/**
 * Repository pour la gestion des thèmes
 */
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    /**
     * Recherche un thème par son code
     */
    Optional<Theme> findByCode(String code);

    /**
     * Recherche les thèmes actifs triés par ordre d'index
     */
    List<Theme> findByStatusOrderByOrderIndexAsc(String status);

    /**
     * Recherche les thèmes actifs avec leurs types et traductions
     * Triés par ordre croissant sur la colonne `name`
     */
    @Query("SELECT t FROM Theme t LEFT JOIN FETCH t.themeType LEFT JOIN FETCH t.translations WHERE t.status = :status ORDER BY t.name ASC")
    List<Theme> findActiveThemesWithDetails(String status);
}