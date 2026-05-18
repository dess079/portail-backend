package ca.sd.portail.service;

import ca.sd.portail.dto.ApplicationResponseDto;
import ca.sd.portail.dto.CreateApplicationDto;
import ca.sd.portail.dto.UpdateApplicationDto;
import ca.sd.portail.entity.Application;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de mapping entre les entités Application et les DTOs
 * Centralise la conversion pour maintenir la cohérence
 */
@Service
public class ApplicationMappingService {

    /**
     * Convertit une entité Application en ApplicationResponseDto
     * 
     * @param application L'entité à convertir
     * @return Le DTO de réponse
     */
    public ApplicationResponseDto toResponseDto(Application application) {
        if (application == null) {
            return null;
        }
        
        return new ApplicationResponseDto(
            application.getCode(),
            application.getCode(),
            application.getName(),
            application.getDescription(),
            application.getStatus(),
            null, // port
            null, // logPath
            null, // createdAt
            null, // updatedAt
            application.getCreatedBy(),
            null // updatedBy
        );
    }

    /**
     * Convertit une liste d'entités Application en liste de ApplicationResponseDto
     * 
     * @param applications La liste d'entités à convertir
     * @return La liste de DTOs de réponse
     */
    public List<ApplicationResponseDto> toResponseDtoList(List<Application> applications) {
        if (applications == null) {
            return List.of();
        }
        
        return applications.stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

    /**
     * Convertit un CreateApplicationDto en entité Application
     * 
     * @param createDto Le DTO de création
     * @return L'entité Application
     */
    public Application toEntity(CreateApplicationDto createDto) {
        if (createDto == null) {
            return null;
        }
        
        Application application = new Application();
        application.setCode(createDto.getCode().trim());
        application.setName(createDto.getName().trim());
        application.setDescription(createDto.getDescription() != null ? 
            createDto.getDescription().trim() : null);
        application.setStatus(createDto.getStatus().trim().toUpperCase());
        // Removed references to setPort, setLogPath
        
        return application;
    }

    /**
     * Met à jour une entité Application à partir d'un UpdateApplicationDto
     * 
     * @param updateDto Le DTO de mise à jour
     * @return L'entité Application mise à jour
     */
    public Application toEntity(UpdateApplicationDto updateDto) {
        if (updateDto == null) {
            return null;
        }
        
        Application application = new Application();
        application.setCode(updateDto.getCode().trim());
        application.setName(updateDto.getName().trim());
        application.setDescription(updateDto.getDescription() != null ? 
            updateDto.getDescription().trim() : null);
        application.setStatus(updateDto.getStatus().trim().toUpperCase());
        // Removed references to setPort, setLogPath
        
        return application;
    }
}