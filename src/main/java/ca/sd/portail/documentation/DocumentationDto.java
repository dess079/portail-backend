package ca.sd.portail.documentation;

import java.util.List;

public record DocumentationDto(
    Long docId,
    String screenKey,
    String applicationCode,
    String titleFr,
    String titleEn,
    String subtitleFr,
    String subtitleEn,
    String iconName,
    int version,
    List<SectionDto> sections
) {}
