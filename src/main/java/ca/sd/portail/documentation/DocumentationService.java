package ca.sd.portail.documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DocumentationService {

    private static final Logger log = LoggerFactory.getLogger(DocumentationService.class);

    private final DocumentationRepository docRepo;
    private final DocumentationSectionRepository sectionRepo;
    private final DocumentationAuditLogRepository auditRepo;

    public DocumentationService(DocumentationRepository docRepo,
                                 DocumentationSectionRepository sectionRepo,
                                 DocumentationAuditLogRepository auditRepo) {
        this.docRepo = docRepo;
        this.sectionRepo = sectionRepo;
        this.auditRepo = auditRepo;
    }

    @Transactional(readOnly = true)
    public DocumentationDto getDocumentation(String applicationCode, String screenKey) {
        Documentation doc = docRepo.findByApplicationCodeAndScreenKeyAndActiveTrue(applicationCode, screenKey)
            .orElseThrow(() -> new NoSuchElementException(
                "Documentation not found for " + applicationCode + "/" + screenKey));
        List<DocumentationSection> sections = sectionRepo
            .findByDocumentation_DocIdAndActiveTrueOrderByOrderIndexAsc(doc.getDocId());
        return toDto(doc, sections);
    }

    @Transactional(readOnly = true)
    public List<SectionDto> getSections(String applicationCode, String screenKey) {
        Documentation doc = requireDoc(applicationCode, screenKey);
        return sectionRepo.findByDocumentation_DocIdAndActiveTrueOrderByOrderIndexAsc(doc.getDocId())
            .stream().map(this::toSectionDto).toList();
    }

    @Transactional
    public SectionDto createSection(String applicationCode, String screenKey,
                                     SectionCreateRequest req, String changedBy) {
        Documentation doc = requireDoc(applicationCode, screenKey);
        DocumentationSection section = new DocumentationSection();
        section.setDocumentation(doc);
        section.setSectionKey(req.sectionKey());
        section.setHeadingLevel(req.headingLevel());
        section.setOrderIndex(req.orderIndex());
        section.setTitleFr(req.titleFr());
        section.setTitleEn(req.titleEn());
        section.setContentFr(req.contentFr() != null ? req.contentFr() : "");
        section.setContentEn(req.contentEn() != null ? req.contentEn() : "");
        section.setCreatedBy(changedBy);
        DocumentationSection saved = sectionRepo.save(section);
        writeAudit(doc.getDocId(), saved.getSectionId(), "CREATE", changedBy,
            null, null, saved.getContentFr(), saved.getContentEn());
        log.info("Section created: {}/{}/{}", applicationCode, screenKey, req.sectionKey());
        return toSectionDto(saved);
    }

    @Transactional
    public SectionDto updateSection(String applicationCode, String screenKey,
                                     Long sectionId, SectionUpdateRequest req, String changedBy) {
        Documentation doc = requireDoc(applicationCode, screenKey);
        DocumentationSection section = sectionRepo
            .findBySectionIdAndDocumentation_DocId(sectionId, doc.getDocId())
            .orElseThrow(() -> new NoSuchElementException("Section " + sectionId + " not found"));

        String oldFr = section.getContentFr();
        String oldEn = section.getContentEn();

        if (req.titleFr() != null) section.setTitleFr(req.titleFr());
        if (req.titleEn() != null) section.setTitleEn(req.titleEn());
        if (req.contentFr() != null) section.setContentFr(req.contentFr());
        if (req.contentEn() != null) section.setContentEn(req.contentEn());
        if (req.headingLevel() != null) section.setHeadingLevel(req.headingLevel());
        section.setLastModifiedBy(changedBy);
        section.setLastModifiedDate(LocalDateTime.now());

        DocumentationSection saved = sectionRepo.save(section);
        writeAudit(doc.getDocId(), sectionId, "UPDATE", changedBy,
            oldFr, oldEn, saved.getContentFr(), saved.getContentEn());
        return toSectionDto(saved);
    }

    @Transactional
    public void deleteSection(String applicationCode, String screenKey,
                               Long sectionId, String changedBy) {
        Documentation doc = requireDoc(applicationCode, screenKey);
        DocumentationSection section = sectionRepo
            .findBySectionIdAndDocumentation_DocId(sectionId, doc.getDocId())
            .orElseThrow(() -> new NoSuchElementException("Section " + sectionId + " not found"));
        writeAudit(doc.getDocId(), sectionId, "DELETE", changedBy,
            section.getContentFr(), section.getContentEn(), null, null);
        sectionRepo.delete(section);
        log.info("Section deleted: {}", sectionId);
    }

    @Transactional
    public void reorderSections(String applicationCode, String screenKey,
                                 SectionReorderRequest req, String changedBy) {
        Documentation doc = requireDoc(applicationCode, screenKey);
        req.order().forEach(item -> {
            sectionRepo.findBySectionIdAndDocumentation_DocId(item.sectionId(), doc.getDocId())
                .ifPresent(s -> {
                    s.setOrderIndex(item.orderIndex());
                    s.setLastModifiedBy(changedBy);
                    s.setLastModifiedDate(LocalDateTime.now());
                    sectionRepo.save(s);
                });
        });
        writeAudit(doc.getDocId(), null, "REORDER", changedBy, null, null, null, null);
    }

    // ─── Private helpers ────────────────────────────────────────────────────

    private Documentation requireDoc(String applicationCode, String screenKey) {
        return docRepo.findByApplicationCodeAndScreenKeyAndActiveTrue(applicationCode, screenKey)
            .orElseThrow(() -> new NoSuchElementException(
                "Documentation not found for " + applicationCode + "/" + screenKey));
    }

    private void writeAudit(Long docId, Long sectionId, String action, String changedBy,
                             String oldFr, String oldEn, String newFr, String newEn) {
        DocumentationAuditLog entry = new DocumentationAuditLog();
        entry.setDocId(docId);
        entry.setSectionId(sectionId);
        entry.setAction(action);
        entry.setChangedBy(changedBy);
        entry.setOldContentFr(oldFr);
        entry.setOldContentEn(oldEn);
        entry.setNewContentFr(newFr);
        entry.setNewContentEn(newEn);
        auditRepo.save(entry);
    }

    private DocumentationDto toDto(Documentation doc, List<DocumentationSection> sections) {
        return new DocumentationDto(
            doc.getDocId(), doc.getScreenKey(), doc.getApplicationCode(),
            doc.getTitleFr(), doc.getTitleEn(), doc.getSubtitleFr(), doc.getSubtitleEn(),
            doc.getIconName(), doc.getVersion(),
            sections.stream().map(this::toSectionDto).toList()
        );
    }

    private SectionDto toSectionDto(DocumentationSection s) {
        return new SectionDto(
            s.getSectionId(), s.getSectionKey(), s.getHeadingLevel(), s.getOrderIndex(),
            s.getTitleFr(), s.getTitleEn(), s.getContentFr(), s.getContentEn()
        );
    }
}
