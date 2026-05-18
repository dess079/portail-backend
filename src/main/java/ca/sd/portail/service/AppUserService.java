package ca.sd.portail.service;

import ca.sd.portail.dto.UserProfileDto;
import ca.sd.portail.entity.AppUser;
import ca.sd.portail.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des utilisateurs authentifiés via Auth0.
 * <p>
 * Responsabilité unique : synchroniser les informations du JWT Auth0
 * avec la table {@code TB_USR_USERS} (upsert) et retourner un profil.
 * </p>
 */
@Service
public class AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Crée ou met à jour l'utilisateur en base à partir des claims du JWT Auth0.
     * <p>
     * Appelé à chaque requête {@code GET /api/auth/me} pour maintenir les
     * données synchronisées avec Auth0 (nom, photo, email peuvent changer).
     * </p>
     *
     * @param jwt JWT validé par Spring Security (émis par Auth0)
     * @return profil utilisateur à retourner au frontend
     */
    @Transactional
    public UserProfileDto upsertFromJwt(Jwt jwt) {
        // Auth0 garantit que 'sub' est toujours présent et unique
        String sub      = jwt.getSubject();
        String email    = jwt.getClaimAsString("email");
        String name     = jwt.getClaimAsString("name");
        String picture  = jwt.getClaimAsString("picture");
        String provider = extractProvider(sub);

        AppUser user = appUserRepository.findByUsrSub(sub)
                .map(existing -> updateExistingUser(existing, email, name, picture))
                .orElseGet(() -> createNewUser(sub, email, name, picture, provider));

        log.debug("Upsert utilisateur : sub={}, email={}, provider={}", sub, email, provider);

        return toDto(user);
    }

    /**
     * Retourne le profil d'un utilisateur existant sans upsert.
     * Utile pour les appels internes qui n'ont pas besoin de mise à jour.
     *
     * @param sub Subject Auth0
     * @return profil utilisateur, ou null si non trouvé
     */
    @Transactional(readOnly = true)
    public UserProfileDto findBySub(String sub) {
        return appUserRepository.findByUsrSub(sub)
                .map(this::toDto)
                .orElse(null);
    }

    // -------------------------------------------------------------------------
    // Méthodes privées
    // -------------------------------------------------------------------------

    /**
     * Met à jour les champs mutables d'un utilisateur existant.
     * Le sub et le provider ne changent jamais.
     */
    private AppUser updateExistingUser(AppUser user, String email, String name, String picture) {
        user.setUsrEmail(email != null ? email : user.getUsrEmail());
        user.setUsrName(name != null ? name : user.getUsrName());
        user.setUsrPicture(picture);
        return appUserRepository.save(user);
    }

    /** Crée un nouvel utilisateur à partir des claims JWT. */
    private AppUser createNewUser(String sub, String email, String name, String picture, String provider) {
        AppUser newUser = new AppUser(sub, email, name, picture, provider);
        log.info("Nouvel utilisateur créé : email={}, provider={}", email, provider);
        return appUserRepository.save(newUser);
    }

    /**
     * Extrait le fournisseur d'identité depuis le Subject Auth0.
     * Exemples : {@code google-oauth2|xxx} → {@code google},
     *            {@code github|xxx} → {@code github},
     *            {@code auth0|xxx} → {@code local}.
     */
    private String extractProvider(String sub) {
        if (sub == null) return "local";
        if (sub.startsWith("google"))  return "google";
        if (sub.startsWith("github"))  return "github";
        if (sub.startsWith("apple"))   return "apple";
        return "local";
    }

    /** Convertit une entité {@link AppUser} en {@link UserProfileDto}. */
    private UserProfileDto toDto(AppUser user) {
        return new UserProfileDto(
                user.getUsrId(),
                user.getUsrEmail(),
                user.getUsrName(),
                user.getUsrPicture(),
                user.getUsrProvider(),
                Boolean.TRUE.equals(user.getUsrActive())
        );
    }
}
