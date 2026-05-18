package ca.sd.portail.documentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record SectionCreateRequest(
    @NotBlank String sectionKey,
    @Min(2) @Max(3) int headingLevel,
    int orderIndex,
    @NotBlank String titleFr,
    @NotBlank String titleEn,
    String contentFr,
    String contentEn
) {}
