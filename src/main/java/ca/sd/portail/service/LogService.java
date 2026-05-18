package ca.sd.portail.service;

import ca.sd.portail.dto.LogEntryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service pour la lecture et le parsing des fichiers de logs
 * Gère la récupération des logs depuis les fichiers système
 */
@Service
public class LogService {

    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    @Value("${portail.logs.base-path:/logs}")
    private String baseLogsPath;

    // Pattern pour parser les logs Spring Boot format standard
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\s+([A-Z]+)\\s+.*?---\\s+\\[(.+?)\\]\\s+(.+?)\\s+:\\s+(.*)$"
    );

    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Récupère les logs pour une application donnée
     * 
     * @param applicationCode Le code de l'application
     * @param limit Le nombre maximum de lignes à retourner
     * @param level Le niveau de log à filtrer (optionnel)
     * @param searchTerm Terme de recherche dans les messages (optionnel)
     * @return Liste des entrées de log
     */
    public List<LogEntryDto> getLogsForApplication(String applicationCode, Integer limit, 
                                                   String level, String searchTerm) {
        log.debug("Récupération des logs pour l'application: {} avec limit: {}, level: {}, search: {}", 
                  applicationCode, limit, level, searchTerm);

        try {
            Path logFile = getLogFileForApplication(applicationCode);
            if (!Files.exists(logFile)) {
                log.warn("Fichier de log non trouvé pour l'application: {}", applicationCode);
                return Collections.emptyList();
            }

            List<LogEntryDto> logs = readAndParseLogFile(logFile, applicationCode);
            
            // Appliquer les filtres
            logs = applyFilters(logs, level, searchTerm);
            
            // Trier par timestamp décroissant (plus récent en premier)
            logs.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
            
            // Appliquer la limite
            if (limit != null && limit > 0) {
                logs = logs.stream().limit(limit).collect(Collectors.toList());
            }

            return logs;

        } catch (Exception e) {
            log.error("Erreur lors de la lecture des logs pour l'application: {}", applicationCode, e);
            return Collections.emptyList();
        }
    }

    /**
     * Détermine le chemin du fichier de log pour une application
     * 
     * @param applicationCode Le code de l'application
     * @return Le chemin vers le fichier de log
     */
    private Path getLogFileForApplication(String applicationCode) {
        // Mapping des applications vers leurs fichiers de logs
        Map<String, String> logFileMapping = Map.of(
            "portail", "portail/application.log",
            "lottosylvain", "lottosylvain/app.log",
            "lottosylvain", "lottosylvain/application.log",
            "sed-backend", "sed-backend/application.log",
            "sed-frontend", "sed-frontend/access.log"
        );

        String logFileName = logFileMapping.getOrDefault(applicationCode, applicationCode + "/application.log");
        return Paths.get(baseLogsPath, logFileName);
    }

    /**
     * Lit et parse un fichier de log
     * 
     * @param logFile Le fichier de log à lire
     * @param applicationCode Le code de l'application
     * @return Liste des entrées de log parsées
     */
    private List<LogEntryDto> readAndParseLogFile(Path logFile, String applicationCode) throws IOException {
        List<LogEntryDto> logs = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                LogEntryDto entry = parseLogLine(line, applicationCode, logFile.toString(), lineNumber);
                if (entry != null) {
                    logs.add(entry);
                }
            }
        }

        return logs;
    }

    /**
     * Parse une ligne de log individual
     * 
     * @param line La ligne de log à parser
     * @param applicationCode Le code de l'application
     * @param filePath Le chemin du fichier de log
     * @param lineNumber Le numéro de ligne
     * @return L'entrée de log parsée ou null si le parsing échoue
     */
    private LogEntryDto parseLogLine(String line, String applicationCode, String filePath, int lineNumber) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {
            Matcher matcher = LOG_PATTERN.matcher(line);
            
            if (matcher.matches()) {
                String timestamp = matcher.group(1);
                String level = matcher.group(2);
                String thread = matcher.group(3);
                String logger = matcher.group(4);
                String message = matcher.group(5);

                String id = generateLogEntryId(applicationCode, lineNumber, timestamp);

                LogEntryDto.LogLevel logLevel = parseLogLevel(level);

                LogEntryDto entry = new LogEntryDto(id, timestamp, logLevel, message, applicationCode);
                entry.setFilePath(filePath);
                entry.setLineNumber(lineNumber);

                return entry;
            } else {
                // Si le pattern ne match pas, créer une entrée basique
                return createBasicLogEntry(line, applicationCode, filePath, lineNumber);
            }

        } catch (Exception e) {
            log.warn("Erreur lors du parsing de la ligne de log: {}", line, e);
            return createBasicLogEntry(line, applicationCode, filePath, lineNumber);
        }
    }

    /**
     * Crée une entrée de log basique pour les lignes qui ne matchent pas le pattern
     */
    private LogEntryDto createBasicLogEntry(String line, String applicationCode, String filePath, int lineNumber) {
        String id = generateLogEntryId(applicationCode, lineNumber, LocalDateTime.now().toString());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        
        return new LogEntryDto(id, timestamp, LogEntryDto.LogLevel.INFO, line, applicationCode, filePath, lineNumber);
    }

    /**
     * Parse le niveau de log depuis une chaîne
     */
    private LogEntryDto.LogLevel parseLogLevel(String level) {
        try {
            return LogEntryDto.LogLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LogEntryDto.LogLevel.INFO;
        }
    }

    /**
     * Génère un ID unique pour une entrée de log
     */
    private String generateLogEntryId(String applicationCode, int lineNumber, String timestamp) {
        return String.format("%s-%d-%s", applicationCode, lineNumber, timestamp.replaceAll("[^0-9]", ""));
    }

    /**
     * Applique les filtres sur la liste des logs
     */
    private List<LogEntryDto> applyFilters(List<LogEntryDto> logs, String level, String searchTerm) {
        return logs.stream()
                .filter(log -> level == null || log.getLevel().name().equalsIgnoreCase(level))
                .filter(log -> searchTerm == null || searchTerm.trim().isEmpty() || 
                              log.getMessage().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}