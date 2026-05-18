package ca.sd.portail.controller;

import ca.sd.portail.dto.UserProfileDto;
import ca.sd.portail.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour les opérations d'authentification côté portail.
 * <p>
 * Toutes les routes requièrent un Bearer JWT Auth0 valide (sauf /logout qui
 * est sans état et retourne simplement 200).
 * La validation JWT est assurée par {@link ca.sd.portail.config.SecurityConfig}.
 * </p>
 *
 * <pre>
 * GET  /api/auth/me      → profil de l'utilisateur connecté (upsert auto en base)
 * POST /api/auth/logout  → confirmation côté serveur (vrai logout = côté client + Auth0)
 * </pre>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Retourne le profil de l'utilisateur connecté.
     * <p>
     * À chaque appel, les données du profil (nom, photo, email) sont synchronisées
     * depuis le JWT Auth0 vers la base de données (upsert idempotent).
     * </p>
     *
     * @param jwt JWT injecté par Spring Security (validé via JWKS Auth0)
     * @return {@link UserProfileDto} avec les informations de profil
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UserProfileDto profile = appUserService.upsertFromJwt(jwt);
        return ResponseEntity.ok(profile);
    }

    /**
     * Endpoint de logout côté serveur.
     * <p>
     * Le portail étant sans état (stateless JWT), il n'y a pas de session serveur
     * à invalider ici. Le vrai logout se fait côté client (cf. Auth0 logout URL)
     * et dans shared-components ({@code LogoutButton.tsx}).
     * Cet endpoint existe pour offrir un hook futur (audit log, token blacklist…).
     * </p>
     *
     * @return 200 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Stateless — aucune session à détruire côté serveur
        // Futur : ajouter audit log ou token blacklisting ici
        return ResponseEntity.ok().build();
    }
}
