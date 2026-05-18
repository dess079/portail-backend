package ca.sd.portail.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sd.portail.entity.Application;

/**
 * Repository pour la gestion des applications
 * Interface pour les opérations CRUD sur les entités Application
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {

    /**
     * Recherche une application par son code unique
     * 
     * @param code Le code de l'application
     * @return L'application correspondante si elle existe
     */
    Optional<Application> findByCode(String code);

    /**
     * Recherche des applications par statut
     * 
     * @param status Le statut des applications à rechercher
     * @return Liste des applications avec le statut donné
     */
    List<Application> findByStatus(String status);

    /**
     * Recherche des applications par type d'application (ex: 2 = frontend)
     */
    List<Application> findByAppTypeCode(Integer appTypeCode);

    /**
     * Recherche des applications par type et statut
     */
    List<Application> findByAppTypeCodeAndStatus(Integer appTypeCode, String status);


    /**
     * Vérifie si une application avec le code donné existe
     * 
     * @param code Le code à vérifier
     * @return true si une application avec ce code existe
     */
    boolean existsByCode(String code);

    /**
     * Compte le nombre d'applications par statut
     * 
     * @param status Le statut à compter
     * @return Le nombre d'applications avec ce statut
     */
    long countByStatus(String status);
}