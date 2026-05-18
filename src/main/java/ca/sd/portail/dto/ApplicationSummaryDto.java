package ca.sd.portail.dto;

/**
 * DTO simplifié pour les résumés d'applications
 * Utilisé pour afficher la liste des applications sans charger toutes les données
 */
public class ApplicationSummaryDto {

    private String code;
    private String name;
    private String status;

    // Constructeurs
    public ApplicationSummaryDto() {}

    public ApplicationSummaryDto(String code, String name, String status) {
        this.code = code;
        this.name = name;
        this.status = status;
    }

    // Getters et setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}