package ca.sd.portail.dto;

/**
 * DTO pour les paramètres applicatifs (clé-valeur)
 */
public class AppSettingDto {

    private String userId;
    private String key;
    private String value;
    private String type;
    private String description;

    public AppSettingDto() {}

    public AppSettingDto(String userId, String key, String value, String type, String description) {
        this.userId = userId;
        this.key = key;
        this.value = value;
        this.type = type;
        this.description = description;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
