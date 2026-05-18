package ca.sd.portail.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * Entité JPA représentant les préférences utilisateur (langue et thème)
 * Mappée sur la table TB_USER_PREFERENCES
 */
@Entity
@Table(name = "TB_USER_PREFERENCES")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "TEXT")
    private String userId;

    @Column(name = "language", nullable = false, columnDefinition = "TEXT")
    private String language = "fr";

    @Column(name = "theme", nullable = false, columnDefinition = "TEXT")
    private String theme = "default";

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    // Constructors
    public UserPreference() {
        this.createdDate = LocalDateTime.now();
    }

    public UserPreference(String userId, String language, String theme) {
        this.userId = userId;
        this.language = language;
        this.theme = theme;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}