package ca.sd.portail.documentation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DocumentationSectionRepository extends JpaRepository<DocumentationSection, Long> {
    List<DocumentationSection> findByDocumentation_DocIdAndActiveTrueOrderByOrderIndexAsc(Long docId);
    Optional<DocumentationSection> findBySectionIdAndDocumentation_DocId(Long sectionId, Long docId);
}
