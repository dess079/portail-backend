package ca.sd.portail.dto;

/**
 * DTO de profil utilisateur retourné par {@code GET /api/auth/me}.
 * <p>
 * Exposé au frontend — contient uniquement les données nécessaires à
 * l'affichage. Ne jamais exposer {@code usrSub} brut ou des données sensibles.
 * </p>
 *
 * @param id       Identifiant interne (Long, opaque pour le frontend)
 * @param email    Adresse courriel
 * @param name     Nom affiché
 * @param picture  URL de la photo de profil (peut être null)
 * @param provider Fournisseur d'identité : google, apple, github, local
 * @param active   Indique si le compte est actif
 */
public record UserProfileDto(
        Long    id,
        String  email,
        String  name,
        String  picture,
        String  provider,
        boolean active
) {
    // record immuable — pas de setters, hashCode/equals/toString générés automatiquement
}
