package ca.sd.portail.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Entité JPA représentant une application dans le système
 * Mappée sur la table tb_apn_appli_name
 */

@Entity
@Table(name = "TB_APP_APPLICATIONS")

public class Application {

    @Id
    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "icon")
    private String icon;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "app_type_code")
    private Integer appTypeCode;

    @ManyToMany
    @JoinTable(
        name = "TB_APP_ENV_LIAISON",
        joinColumns = @JoinColumn(name = "app_code", referencedColumnName = "code"),
        inverseJoinColumns = @JoinColumn(name = "env_id", referencedColumnName = "env_id")
    )
    private Set<Environnement> environnements;

    /**
     * Méthode appelée avant la persistance de l'entité
     */
    @PrePersist
    protected void onCreate() {
    }

    /**
     * Méthode appelée avant la mise à jour de l'entité
     */
    @PreUpdate
    protected void onUpdate() {
    }

    // Constructeurs

    public Application() {}

    public Application(String code, String name, String description, String status) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // Getters et Setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Integer getAppTypeCode() {
        return appTypeCode;
    }

    public void setAppTypeCode(Integer appTypeCode) {
        this.appTypeCode = appTypeCode;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "Application{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}