package ca.sd.portail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 * Configuration Spring Security pour le backend Portail.
 * <p>
 * Architecture : <b>OAuth2 Resource Server stateless</b>.
 * Le backend ne gère pas de sessions ni de cookies d'authentification.
 * Chaque requête doit contenir un Bearer JWT signé par Auth0.
 * </p>
 *
 * <h3>Flux complet :</h3>
 * <ol>
 *   <li>Frontend (portail) redirige vers Auth0 Universal Login</li>
 *   <li>Auth0 retourne un JWT signé au frontend</li>
 *   <li>Frontend envoie {@code Authorization: Bearer <token>} à chaque appel API</li>
 *   <li>Spring Security valide la signature via JWKS Auth0 ({@code /.well-known/jwks.json})</li>
 *   <li>Si valide → requête traitée ; sinon → 401</li>
 * </ol>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Active/désactive l'authentification JWT.
     * Utilisé surtout en local pour faciliter les tests frontend.
     */
    @Value("${portail.security.auth-enabled:true}")
    private boolean authEnabled;

    /**
     * Audience attendue dans le claim {@code aud} du JWT.
     * Doit correspondre à l'identifier de l'API Auth0.
     * Valeur par défaut : {@code https://portail-api}.
     */
    @Value("${portail.auth.audience:https://portail-api}")
    private String audience;

    /**
     * Issuer URI Auth0. Spring Security télécharge automatiquement
     * le fichier JWKS depuis {@code {issuerUri}/.well-known/jwks.json}.
     */
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    /**
     * Chaîne de filtres de sécurité principale.
     * <p>
     * Routes publiques (aucun token requis) :
     * <ul>
     *   <li>{@code GET /actuator/health} — sondes de santé Docker/Cloudflare</li>
     *   <li>{@code GET /api/public/**}   — endpoints sans authentification futurs</li>
     * </ul>
     * Toutes les autres routes exigent un Bearer JWT valide.
     * </p>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (!authEnabled) {
            http
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

            return http.build();
        }

        http
            // CORS : délègue à la configuration définie dans WebConfig
            .cors(Customizer.withDefaults())

            // API stateless : pas de session HTTP côté serveur
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // CSRF inutile pour une API REST stateless (pas de cookies de session)
            .csrf(AbstractHttpConfigurer::disable)

            // Autorisation par route
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                // Endpoints appelés avant authentification (ThemeProvider, LanguageProvider, AppUrlsProvider)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/preferences").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/themes").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/themes/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/themes/current").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/applications").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/applications/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/documentation/**").permitAll()
                .anyRequest().authenticated()
            )

            // Validation JWT via JWKS Auth0 avec vérification de l'audience
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );

        return http.build();
    }

    /**
     * Décodeur JWT personnalisé qui valide :
     * <ul>
     *   <li>La signature via JWKS Auth0 (automatique)</li>
     *   <li>L'expiration (automatique)</li>
     *   <li>L'issuer (doit correspondre à {@code spring.security.oauth2.resourceserver.jwt.issuer-uri})</li>
     *   <li>L'audience (doit contenir {@code portail.auth.audience})</li>
     * </ul>
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuerUri);

        // Validateur d'audience custom (Auth0 ne met pas aud dans les tokens par défaut
        // sans qu'une API soit définie — ce validateur l'exige explicitement)
        OAuth2TokenValidator<Jwt> audienceValidator = token -> {
            List<String> audiences = token.getAudience();
            if (audiences != null && audiences.contains(audience)) {
                return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success();
            }
            return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure(
                new org.springframework.security.oauth2.core.OAuth2Error(
                    "invalid_token",
                    "Le token ne contient pas l'audience requise: " + audience,
                    null
                )
            );
        };

        OAuth2TokenValidator<Jwt> withIssuer   = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        decoder.setJwtValidator(withAudience);
        return decoder;
    }
}
