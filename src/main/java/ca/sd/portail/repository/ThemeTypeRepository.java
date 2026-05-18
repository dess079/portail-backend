package ca.sd.portail.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sd.portail.entity.ThemeType;

/**
 * Repository pour la gestion des types de thèmes
 */
@Repository
public interface ThemeTypeRepository extends JpaRepository<ThemeType, Long> {

    /**
     * Recherche un type de thème par son code
     */
    Optional<ThemeType> findByCode(String code);

    /**
     * Recherche les types de thèmes actifs
     */
    List<ThemeType> findByStatus(String status);
}