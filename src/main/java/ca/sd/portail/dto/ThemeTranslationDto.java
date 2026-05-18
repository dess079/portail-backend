package ca.sd.portail.dto;

/**
 * DTO pour les données de traduction de thème
 */
public class ThemeTranslationDto {

    private String languageCode;
    private String name;

    // Constructors
    public ThemeTranslationDto() {}

    public ThemeTranslationDto(String languageCode, String name) {
        this.languageCode = languageCode;
        this.name = name;
    }

    // Getters and Setters
    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}