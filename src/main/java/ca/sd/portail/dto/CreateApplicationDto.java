package ca.sd.portail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * DTO pour la création d'une nouvelle application
 * Contient les champs requis pour créer une application
 */
public class CreateApplicationDto {

    @NotBlank(message = "Le code de l'application est obligatoire")
    @Size(min = 2, max = 50, message = "Le code doit contenir entre 2 et 50 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Le code ne peut contenir que des lettres, chiffres, tirets et underscores")
    private String code;

    @NotBlank(message = "Le nom de l'application est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    @Size(max = 500, message = "La description ne peut dépasser 500 caractères")
    private String description;

    @NotBlank(message = "Le statut de l'application est obligatoire")
    @Pattern(regexp = "^(ACTIVE|INACTIVE|MAINTENANCE)$", message = "Le statut doit être ACTIVE, INACTIVE ou MAINTENANCE")
    private String status = "ACTIVE";

    @Min(value = 1000, message = "Le port doit être supérieur à 999")
    @Max(value = 65535, message = "Le port doit être inférieur à 65536")
    private Integer port;

    @Size(max = 255, message = "Le chemin de log ne peut dépasser 255 caractères")
    private String logPath;

    /**
     * Constructeur par défaut
     */
    public CreateApplicationDto() {
        this.status = "ACTIVE";
    }

    /**
     * Constructeur avec les champs obligatoires
     */
    public CreateApplicationDto(String code, String name, String description) {
        this();
        this.code = code;
        this.name = name;
        this.description = description;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    @Override
    public String toString() {
        return "CreateApplicationDto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", port=" + port +
                '}';
    }
}