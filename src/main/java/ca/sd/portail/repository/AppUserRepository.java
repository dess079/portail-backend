package ca.sd.portail.repository;

import ca.sd.portail.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link AppUser}.
 * <p>
 * Utilise les méthodes dérivées Spring Data pour les recherches naturelles
 * dans le flux d'authentification (recherche par sub ou email).
 * </p>
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Recherche un utilisateur par son Subject Auth0 (clé stable JWT).
     *
     * @param usrSub Subject Auth0, ex : {@code google-oauth2|12345}
     * @return l'utilisateur si trouvé
     */
    Optional<AppUser> findByUsrSub(String usrSub);

    /**
     * Recherche un utilisateur par son adresse courriel.
     * Utilisé en fallback ou pour la fusion de comptes.
     *
     * @param usrEmail adresse courriel
     * @return l'utilisateur si trouvé
     */
    Optional<AppUser> findByUsrEmail(String usrEmail);
}
