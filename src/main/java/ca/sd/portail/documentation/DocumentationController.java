package ca.sd.portail.documentation;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST Controller for screen documentation.
 * GET endpoints are public (no Auth0 required).
 * POST/PUT/DELETE endpoints require a valid Auth0 JWT.
 */
@RestController
@RequestMapping("/api/v1/documentation")
public class DocumentationController {

    private static final Logger log = LoggerFactory.getLogger(DocumentationController.class);

    private final DocumentationService service;

    public DocumentationController(DocumentationService service) {
        this.service = service;
    }

    /** Lecture publique — retourne doc + sections */
    @GetMapping("/{applicationCode}/{screenKey}")
    public ResponseEntity<DocumentationDto> getDocumentation(
            @PathVariable String applicationCode,
            @PathVariable String screenKey) {
        log.info("GET /api/v1/documentation/{}/{}", applicationCode, screenKey);
        try {
            return ResponseEntity.ok(service.getDocumentation(applicationCode, screenKey));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Liste des sections (lecture publique) */
    @GetMapping("/{applicationCode}/{screenKey}/sections")
    public ResponseEntity<List<SectionDto>> getSections(
            @PathVariable String applicationCode,
            @PathVariable String screenKey) {
        try {
            return ResponseEntity.ok(service.getSections(applicationCode, screenKey));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Créer une section — Auth0 requis */
    @PostMapping("/{applicationCode}/{screenKey}/sections")
    public ResponseEntity<SectionDto> createSection(
            @PathVariable String applicationCode,
            @PathVariable String screenKey,
            @Valid @RequestBody SectionCreateRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        String changedBy = jwt != null ? jwt.getSubject() : "unknown";
        try {
            SectionDto created = service.createSection(applicationCode, screenKey, req, changedBy);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Modifier une section — Auth0 requis */
    @PutMapping("/{applicationCode}/{screenKey}/sections/{sectionId}")
    public ResponseEntity<SectionDto> updateSection(
            @PathVariable String applicationCode,
            @PathVariable String screenKey,
            @PathVariable Long sectionId,
            @RequestBody SectionUpdateRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        String changedBy = jwt != null ? jwt.getSubject() : "unknown";
        try {
            return ResponseEntity.ok(service.updateSection(applicationCode, screenKey, sectionId, req, changedBy));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Supprimer une section — Auth0 requis */
    @DeleteMapping("/{applicationCode}/{screenKey}/sections/{sectionId}")
    public ResponseEntity<Void> deleteSection(
            @PathVariable String applicationCode,
            @PathVariable String screenKey,
            @PathVariable Long sectionId,
            @AuthenticationPrincipal Jwt jwt) {
        String changedBy = jwt != null ? jwt.getSubject() : "unknown";
        try {
            service.deleteSection(applicationCode, screenKey, sectionId, changedBy);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Réordonner les sections — Auth0 requis */
    @PutMapping("/{applicationCode}/{screenKey}/sections/reorder")
    public ResponseEntity<Void> reorderSections(
            @PathVariable String applicationCode,
            @PathVariable String screenKey,
            @RequestBody SectionReorderRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        String changedBy = jwt != null ? jwt.getSubject() : "unknown";
        try {
            service.reorderSections(applicationCode, screenKey, req, changedBy);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
