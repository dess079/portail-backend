package ca.sd.portail.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.dto.ApplicationDto;
import ca.sd.portail.dto.ApplicationSummaryDto;
import ca.sd.portail.entity.Application;
import ca.sd.portail.repository.ApplicationRepository;
import ca.sd.portail.repository.ApplicationEnvironnementRepository;
import ca.sd.portail.util.ApplicationMapper;

/**
 * Service métier pour la gestion des applications
 * Implémente la logique business pour les opérations CRUD
 */
@Service
@Transactional
public class ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final Environment environment;
    private final ApplicationEnvironnementRepository applicationEnvironnementRepository;

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper, Environment environment, ApplicationEnvironnementRepository applicationEnvironnementRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.environment = environment;
        this.applicationEnvironnementRepository = applicationEnvironnementRepository;
    }

    /**
     * Adapte l'URL d'une application selon l'environnement actif en consultant la base de données
     */
    private String adaptUrlForEnvironment(String appCode, String environmentOverride) {
        String envCode = (environmentOverride != null && !environmentOverride.trim().isEmpty()) ? environmentOverride : detectEnvironmentCode();
        log.debug("Adapting URL for app '{}' in environment '{}'", appCode, envCode);
        
        try {
            String url = applicationEnvironnementRepository.findUrlByAppCodeAndEnvCode(appCode, envCode)
                    .orElseThrow(() -> new RuntimeException("URL non trouvée pour l'application " + appCode + " dans l'environnement " + envCode));
            log.debug("Found URL '{}' for app '{}' in environment '{}'", url, appCode, envCode);
            return url;
        } catch (Exception e) {
            log.warn("Impossible d'adapter l'URL pour l'application {} dans l'environnement {}: {}", appCode, envCode, e.getMessage());
            throw e;
        }
    }

    /**
     * Détecte le code d'environnement actif
     */
    private String detectEnvironmentCode() {
        String[] activeProfiles = environment.getActiveProfiles();

        // Vérifie d'abord le profil cloud
        for (String profile : activeProfiles) {
            if ("cloud".equals(profile)) {
                return "cloud";
            }
        }

        // Puis le profil container
        for (String profile : activeProfiles) {
            if ("container".equals(profile)) {
                return "container";
            }
        }

        // Par défaut, local
        return "local";
    }

    /**
     * Récupère toutes les applications
     * 
     * @param environmentOverride Override optionnel de l'environnement détecté
     * @return Liste de toutes les applications
     */
    @Transactional(readOnly = true)
    public List<ApplicationDto> getAllApplications(String environmentOverride) {
        log.debug("Récupération de toutes les applications - environment: {}", environmentOverride);
        // Retourner uniquement les applications de type 'frontend' (code = 2) pour l'écran du portail
        List<Application> applications = applicationRepository.findByAppTypeCode(2);
        return applications.stream()
                .map(app -> {
                    ApplicationDto dto = applicationMapper.toDto(app);
                    // Adapter l'URL selon l'environnement actif
                    try {
                        String adaptedUrl = adaptUrlForEnvironment(app.getCode(), environmentOverride);
                        dto.setUrl(adaptedUrl);
                    } catch (RuntimeException e) {
                        log.info("Impossible d'adapter l'URL pour l'application {} dans l'environnement {}: {}", 
                                app.getCode(), environmentOverride != null ? environmentOverride : detectEnvironmentCode(), e.getMessage());
                        // En cas d'erreur, garde l'URL par défaut mais log une erreur
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère les applications par statut
     * 
     * @param status Le statut des applications à récupérer
     * @param environmentOverride Override optionnel de l'environnement détecté
     * @return Liste des applications avec le statut donné
     */
    @Transactional(readOnly = true)
    public List<ApplicationDto> getApplicationsByStatus(String status, String environmentOverride) {
        log.debug("Récupération des applications avec le statut: {} - environment: {}", status, environmentOverride);
        // Lorsque l'on filtre par statut, respecter aussi le type 'frontend' pour l'affichage du portail
        List<Application> applications = applicationRepository.findByAppTypeCodeAndStatus(2, status);
        return applications.stream()
                .map(app -> {
                    ApplicationDto dto = applicationMapper.toDto(app);
                    // Adapter l'URL selon l'environnement actif
                    try {
                        String adaptedUrl = adaptUrlForEnvironment(app.getCode(), environmentOverride);
                        dto.setUrl(adaptedUrl);
                    } catch (RuntimeException e) {
                        log.warn("Impossible d'adapter l'URL pour l'application {}: {}", app.getCode(), e.getMessage());
                        // Garde l'URL par défaut
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère une application par son code
     * 
     * @param code Le code de l'application
     * @param environmentOverride Override optionnel de l'environnement détecté
     * @return L'application si elle existe
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationDto> getApplicationByCode(String code, String environmentOverride) {
        log.debug("Récupération de l'application avec le code: {} - environment: {}", code, environmentOverride);
        
        return applicationRepository.findByCode(code)
                .map(app -> {
                    ApplicationDto dto = applicationMapper.toDto(app);
                    // Adapter l'URL selon l'environnement actif
                    try {
                        String adaptedUrl = adaptUrlForEnvironment(app.getCode(), environmentOverride);
                        dto.setUrl(adaptedUrl);
                    } catch (RuntimeException e) {
                        log.warn("Impossible d'adapter l'URL pour l'application {}: {}", app.getCode(), e.getMessage());
                        // Garde l'URL par défaut
                    }
                    return dto;
                });
    }

    /**
     * Crée une nouvelle application
     * 
     * @param applicationDto Les données de l'application à créer
     * @return L'application créée
     * @throws IllegalArgumentException si une application avec le même code existe déjà
     */
    public ApplicationDto createApplication(ApplicationDto applicationDto) {
        log.info("Création d'une nouvelle application avec le code: {}", applicationDto.getCode());
        
        // Vérifier que le code n'existe pas déjà
        if (applicationRepository.existsByCode(applicationDto.getCode())) {
            throw new IllegalArgumentException("Une application avec le code " + applicationDto.getCode() + " existe déjà");
        }
        
        Application application = applicationMapper.toEntity(applicationDto);
        Application savedApplication = applicationRepository.save(application);
        
        log.info("Application créée avec succès: {}", savedApplication.getCode());
        return applicationMapper.toDto(savedApplication);
    }

    /**
     * Met à jour une application existante
     * 
     * @param code Le code de l'application à mettre à jour
     * @param applicationDto Les nouvelles données
     * @return L'application mise à jour
     * @throws RuntimeException si l'application n'existe pas
     */
    public ApplicationDto updateApplication(String code, ApplicationDto applicationDto) {
        log.info("Mise à jour de l'application avec le code: {}", code);
        
        Application existingApplication = applicationRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Application non trouvée avec le code: " + code));
        
        // Mettre à jour les champs
        existingApplication.setName(applicationDto.getName());
        existingApplication.setDescription(applicationDto.getDescription());
        existingApplication.setStatus(applicationDto.getStatus());

        
        Application updatedApplication = applicationRepository.save(existingApplication);
        
        log.info("Application mise à jour avec succès: {}", updatedApplication.getCode());
        return applicationMapper.toDto(updatedApplication);
    }

    /**
     * Supprime une application
     * 
     * @param code Le code de l'application à supprimer
     * @throws RuntimeException si l'application n'existe pas
     */
    public void deleteApplication(String code) {
        log.info("Suppression de l'application avec le code: {}", code);
        
        Application application = applicationRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Application non trouvée avec le code: " + code));
        
        applicationRepository.delete(application);
        log.info("Application supprimée avec succès avec le code: {}", code);
    }

    /**
     * Récupère les résumés des applications (code, nom et statut)
     * 
     * @param statusFilter Filtre optionnel par statut
     * @return Liste des résumés d'applications
     */
    @Transactional(readOnly = true)
    public List<ApplicationSummaryDto> getApplicationSummaries(String statusFilter) {
        log.debug("Récupération des résumés d'applications - statusFilter: {}", statusFilter);
        
        List<Application> applications;
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            applications = applicationRepository.findByStatus(statusFilter);
        } else {
            applications = applicationRepository.findAll();
        }
        
        return applications.stream()
                .map(app -> new ApplicationSummaryDto(app.getCode(), app.getName(), app.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Compte le nombre d'applications par statut
     * 
     * @param status Le statut à compter
     * @return Le nombre d'applications
     */
    @Transactional(readOnly = true)
    public long countApplicationsByStatus(String status) {
        log.debug("Comptage des applications avec le statut: {}", status);
        return applicationRepository.countByStatus(status);
    }
}