package ca.sd.portail.dto;

import java.util.List;

/**
 * DTO pour les données de thème exposées via les APIs REST
 */
public class ThemeDto {

    private String code;
    private String name;
    private String type;
    private String definition;
    private List<ThemeTranslationDto> translations;

    // Constructors
    public ThemeDto() {}

    public ThemeDto(String code, String name, String type, String definition, List<ThemeTranslationDto> translations) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.definition = definition;
        this.translations = translations;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDefinition() { return definition; }
    public void setDefinition(String definition) { this.definition = definition; }

    public List<ThemeTranslationDto> getTranslations() { return translations; }
    public void setTranslations(List<ThemeTranslationDto> translations) { this.translations = translations; }
}