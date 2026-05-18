package ca.sd.portail.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité JPA représentant un utilisateur authentifié via Auth0.
 * <p>
 * Mappée sur la table TB_USR_USERS (créée par 02-init-auth.sql).
 * La clé stable côté Auth0 est {@code usrSub} (Subject JWT) — jamais l'email
 * seul, car un utilisateur peut changer d'email.
 * </p>
 *
 * Convention de nommage : toutes les colonnes sont préfixées {@code usr_},
 * les tables en majuscules {@code TB_USR_XXX}, cohérent avec le reste du projet.
 */
@Entity
@Table(name = "TB_USR_USERS")
public class AppUser {

    /** Identifiant technique auto-généré (clé primaire interne). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    private Long usrId;

    /**
     * Subject Auth0 — identifiant unique et stable fourni par Auth0.
     * Format : {@code provider|userId}, ex : {@code google-oauth2|123456789}.
     */
    @Column(name = "usr_sub", nullable = false, unique = true, length = 255)
    private String usrSub;

    /** Adresse courriel de l'utilisateur (peut changer, utiliser usrSub comme clé). */
    @Column(name = "usr_email", nullable = false, length = 255)
    private String usrEmail;

    /** Nom complet ou pseudo affiché dans l'interface. */
    @Column(name = "usr_name", length = 255)
    private String usrName;

    /** URL de la photo de profil (Google, GitHub, etc.). */
    @Column(name = "usr_picture", length = 500)
    private String usrPicture;

    /**
     * Fournisseur d'identité utilisé lors du premier login.
     * Valeurs : {@code google}, {@code apple}, {@code github}, {@code local}.
     */
    @Column(name = "usr_provider", nullable = false, length = 50)
    private String usrProvider;

    /** Indique si le compte est actif (désactivé = accès refusé sans supprimer les données). */
    @Column(name = "usr_active", nullable = false)
    private Boolean usrActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /** Constructeur par défaut requis par JPA. */
    public AppUser() {}

    /**
     * Constructeur de commodité pour la création initiale.
     *
     * @param usrSub      Subject Auth0
     * @param usrEmail    Adresse courriel
     * @param usrName     Nom complet
     * @param usrPicture  URL de photo de profil (peut être null)
     * @param usrProvider Fournisseur d'identité
     */
    public AppUser(String usrSub, String usrEmail, String usrName, String usrPicture, String usrProvider) {
        this.usrSub = usrSub;
        this.usrEmail = usrEmail;
        this.usrName = usrName;
        this.usrPicture = usrPicture;
        this.usrProvider = usrProvider;
    }

    /** Initialise les champs d'audit à la création de l'entité. */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = "system";
        this.usrActive = Boolean.TRUE;
        if (this.usrProvider == null) {
            this.usrProvider = "local";
        }
    }

    /** Met à jour le timestamp d'audit lors d'une modification. */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = "system";
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public Long getUsrId()                        { return usrId; }
    public void setUsrId(Long usrId)              { this.usrId = usrId; }

    public String getUsrSub()                     { return usrSub; }
    public void setUsrSub(String usrSub)          { this.usrSub = usrSub; }

    public String getUsrEmail()                   { return usrEmail; }
    public void setUsrEmail(String usrEmail)      { this.usrEmail = usrEmail; }

    public String getUsrName()                    { return usrName; }
    public void setUsrName(String usrName)        { this.usrName = usrName; }

    public String getUsrPicture()                 { return usrPicture; }
    public void setUsrPicture(String usrPicture)  { this.usrPicture = usrPicture; }

    public String getUsrProvider()                { return usrProvider; }
    public void setUsrProvider(String usrProvider){ this.usrProvider = usrProvider; }

    public Boolean getUsrActive()                 { return usrActive; }
    public void setUsrActive(Boolean usrActive)   { this.usrActive = usrActive; }

    public LocalDateTime getCreatedAt()           { return createdAt; }
    public LocalDateTime getUpdatedAt()           { return updatedAt; }

    public String getCreatedBy()                  { return createdBy; }
    public String getUpdatedBy()                  { return updatedBy; }

    @Override
    public String toString() {
        return "AppUser{usrId=" + usrId + ", usrEmail='" + usrEmail + "', usrProvider='" + usrProvider + "'}";
    }
}
