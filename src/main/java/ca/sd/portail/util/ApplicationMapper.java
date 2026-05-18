package ca.sd.portail.util;

import ca.sd.portail.dto.ApplicationDto;
import ca.sd.portail.entity.Application;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Mapper pour convertir entre les entités Application et les DTOs
 * Sépare la logique de conversion dans une classe dédiée
 */
@Component
public class ApplicationMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Convertit une entité Application en DTO
     * 
     * @param application L'entité à convertir
     * @return Le DTO correspondant
     */
    public ApplicationDto toDto(Application application) {
        if (application == null) {
            return null;
        }

        ApplicationDto dto = new ApplicationDto();
        dto.setId(application.getCode());
        dto.setCode(application.getCode());
        dto.setName(application.getName());
        dto.setDescription(application.getDescription());
        dto.setStatus(application.getStatus());
        dto.setUrl(application.getUrl());
        
        // Conversion des dates LocalDateTime vers String
        if (application.getCreatedDate() != null) {
            dto.setCreatedAt(application.getCreatedDate().format(DATE_FORMATTER));
        }
        if (application.getLastModifiedDate() != null) {
            dto.setUpdatedAt(application.getLastModifiedDate().format(DATE_FORMATTER));
        }

        return dto;
    }

    /**
     * Convertit un DTO ApplicationDto en entité
     * 
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Application toEntity(ApplicationDto dto) {
        if (dto == null) {
            return null;
        }

        Application application = new Application();
        application.setCode(dto.getId());
        application.setCode(dto.getCode());
        application.setName(dto.getName());
        application.setDescription(dto.getDescription());
        application.setStatus(dto.getStatus());
        application.setUrl(dto.getUrl());

        return application;
    }

    /**
     * Met à jour une entité existante avec les données du DTO
     * 
     * @param entity L'entité à mettre à jour
     * @param dto Le DTO contenant les nouvelles données
     */
    public void updateEntityFromDto(Application entity, ApplicationDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setUrl(dto.getUrl());
    }
}