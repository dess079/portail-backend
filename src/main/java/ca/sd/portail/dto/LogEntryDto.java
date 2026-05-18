package ca.sd.portail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour les entrées de log exposées via les APIs REST
 * Représente une ligne de log avec ses métadonnées
 */
public class LogEntryDto {

    private String id;

    private String timestamp;

    private LogLevel level;

    private String message;

    private String service;

    @JsonProperty("filePath")
    private String filePath;

    @JsonProperty("lineNumber")
    private Integer lineNumber;

    /**
     * Énumération des niveaux de log supportés
     */
    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    // Constructeurs
    public LogEntryDto() {}

    public LogEntryDto(String id, String timestamp, LogLevel level, String message, String service) {
        this.id = id;
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.service = service;
    }

    /**
     * Constructeur avec tous les paramètres
     */
    public LogEntryDto(String id, String timestamp, LogLevel level, String message, 
                      String service, String filePath, Integer lineNumber) {
        this(id, timestamp, level, message, service);
        this.filePath = filePath;
        this.lineNumber = lineNumber;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "LogEntryDto{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", level=" + level +
                ", service='" + service + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}