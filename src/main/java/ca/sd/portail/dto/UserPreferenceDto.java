package ca.sd.portail.dto;

/**
 * DTO pour les préférences utilisateur
 */
public class UserPreferenceDto {

    private String userId;
    private String language;
    private String theme;

    // Constructors
    public UserPreferenceDto() {}

    public UserPreferenceDto(String userId, String language, String theme) {
        this.userId = userId;
        this.language = language;
        this.theme = theme;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
}