package ca.sd.portail.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sd.portail.entity.AppSetting;

/**
 * Repository pour la gestion des paramètres applicatifs
 */
@Repository
public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {

    /**
     * Récupère tous les paramètres d'un utilisateur
     */
    List<AppSetting> findByUserId(String userId);

    /**
     * Récupère un paramètre spécifique par utilisateur et clé
     */
    Optional<AppSetting> findByUserIdAndSettingKey(String userId, String settingKey);

    /**
     * Supprime un paramètre spécifique par utilisateur et clé
     */
    void deleteByUserIdAndSettingKey(String userId, String settingKey);
}
