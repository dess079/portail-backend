package ca.sd.portail.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Requête de suppression en masse de plans d'agents.
 */
public record BulkDeleteRequest(
    @NotEmpty @Size(max = 200) List<String> planIds
) {}
