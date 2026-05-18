package ca.sd.portail.repository;

import ca.sd.portail.entity.ApplicationEnvironnement;
import ca.sd.portail.entity.ApplicationEnvironnementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour la gestion des liaisons application-environnement
 */
@Repository
public interface ApplicationEnvironnementRepository extends JpaRepository<ApplicationEnvironnement, ApplicationEnvironnementId> {

    /**
     * Recherche l'URL d'une application pour un environnement donné
     * 
     * @param appCode Le code de l'application
     * @param envCode Le code de l'environnement
     * @return L'URL correspondante si elle existe
     */
    @Query("SELECT ael.url FROM ApplicationEnvironnement ael WHERE ael.id.appCode = :appCode AND ael.environnement.code = :envCode")
    Optional<String> findUrlByAppCodeAndEnvCode(@Param("appCode") String appCode, @Param("envCode") String envCode);
}