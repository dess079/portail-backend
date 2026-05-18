package ca.sd.portail.documentation;

public record SectionDto(
    Long sectionId,
    String sectionKey,
    int headingLevel,
    int orderIndex,
    String titleFr,
    String titleEn,
    String contentFr,
    String contentEn
) {}
