package ca.sd.portail.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sd.portail.entity.UserPreference;

/**
 * Repository pour la gestion des préférences utilisateur
 */
@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    /**
     * Recherche la préférence la plus récente d'un utilisateur par son ID.
     *
     * @param userId L'identifiant utilisateur.
     * @return La préférence la plus récente si elle existe.
     */
    Optional<UserPreference> findTopByUserIdOrderByIdDesc(String userId);
}