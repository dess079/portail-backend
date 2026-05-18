package ca.sd.portail.dto;

/**
 * DTO pour la requête de définition du thème actuel
 */
public class ThemeRequestDto {
    private String theme;

    public ThemeRequestDto() {}

    public ThemeRequestDto(String theme) {
        this.theme = theme;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}