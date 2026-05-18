package ca.sd.portail.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Entité JPA représentant un thème UI
 * Mappée sur la table TB_UI_THEMES
 */
@Entity
@Table(name = "TB_UI_THEMES")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_type_id", nullable = false)
    private ThemeType themeType;

    @Column(name = "theme_definition", columnDefinition = "jsonb", nullable = false)
    private String themeDefinition;

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ThemeTranslation> translations;

    // Constructors
    public Theme() {}

    public Theme(String code, String name, ThemeType themeType, String themeDefinition, String createdBy) {
        this.code = code;
        this.name = name;
        this.themeType = themeType;
        this.themeDefinition = themeDefinition;
        this.createdBy = createdBy;
        this.createdDate = LocalDateTime.now();
        this.status = "ACTIVE";
        this.orderIndex = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ThemeType getThemeType() { return themeType; }
    public void setThemeType(ThemeType themeType) { this.themeType = themeType; }

    public String getThemeDefinition() { return themeDefinition; }
    public void setThemeDefinition(String themeDefinition) { this.themeDefinition = themeDefinition; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public Set<ThemeTranslation> getTranslations() { return translations; }
    public void setTranslations(Set<ThemeTranslation> translations) { this.translations = translations; }
}