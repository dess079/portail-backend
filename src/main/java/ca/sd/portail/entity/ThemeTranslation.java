package ca.sd.portail.entity;

import jakarta.persistence.*;

/**
 * Entité JPA représentant une traduction de thème UI
 * Mappée sur la table TB_UI_THEME_TRANSLATIONS
 */
@Entity
@Table(name = "TB_UI_THEME_TRANSLATIONS")
public class ThemeTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "name", nullable = false)
    private String name;

    // Constructors
    public ThemeTranslation() {}

    public ThemeTranslation(Theme theme, String languageCode, String name) {
        this.theme = theme;
        this.languageCode = languageCode;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Theme getTheme() { return theme; }
    public void setTheme(Theme theme) { this.theme = theme; }

    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}