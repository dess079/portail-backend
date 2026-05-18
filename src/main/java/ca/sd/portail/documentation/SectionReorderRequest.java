package ca.sd.portail.documentation;

import java.util.List;

public record SectionReorderRequest(
    List<SectionOrderItem> order
) {
    public record SectionOrderItem(Long sectionId, int orderIndex) {}
}
