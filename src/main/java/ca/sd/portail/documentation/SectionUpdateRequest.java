package ca.sd.portail.documentation;

public record SectionUpdateRequest(
    String titleFr,
    String titleEn,
    String contentFr,
    String contentEn,
    Integer headingLevel
) {}
