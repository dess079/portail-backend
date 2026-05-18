package ca.sd.portail.dto;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse des APIs Application
 * Représente les données d'une application à retourner au client
 */
public class ApplicationResponseDto {

    private String id;
    private String code;
    private String name;
    private String description;
    private String status;
    private Integer port;
    private String logPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    /**
     * Constructeur par défaut
     */
    public ApplicationResponseDto() {
    }

    /**
     * Constructeur avec tous les champs
     */
    public ApplicationResponseDto(String id, String code, String name, String description,
                                 String status, Integer port, String logPath,
                                 LocalDateTime createdAt, LocalDateTime updatedAt,
                                 String createdBy, String updatedBy) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.status = status;
        this.port = port;
        this.logPath = logPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "ApplicationResponseDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", port=" + port +
                '}';
    }
}