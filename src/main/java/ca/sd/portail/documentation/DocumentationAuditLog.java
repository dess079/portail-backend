package ca.sd.portail.documentation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_DOC_AUDIT_LOG")
public class DocumentationAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "doc_id", nullable = false)
    private Long docId;

    @Column(name = "section_id")
    private Long sectionId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt = LocalDateTime.now();

    @Column(name = "old_content_fr", columnDefinition = "TEXT")
    private String oldContentFr;

    @Column(name = "old_content_en", columnDefinition = "TEXT")
    private String oldContentEn;

    @Column(name = "new_content_fr", columnDefinition = "TEXT")
    private String newContentFr;

    @Column(name = "new_content_en", columnDefinition = "TEXT")
    private String newContentEn;

    @Column(name = "extra_data")
    private String extraData;

    // Getters and setters
    public Long getAuditId() { return auditId; }
    public Long getDocId() { return docId; }
    public void setDocId(Long docId) { this.docId = docId; }
    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getOldContentFr() { return oldContentFr; }
    public void setOldContentFr(String s) { this.oldContentFr = s; }
    public String getOldContentEn() { return oldContentEn; }
    public void setOldContentEn(String s) { this.oldContentEn = s; }
    public String getNewContentFr() { return newContentFr; }
    public void setNewContentFr(String s) { this.newContentFr = s; }
    public String getNewContentEn() { return newContentEn; }
    public void setNewContentEn(String s) { this.newContentEn = s; }
    public String getExtraData() { return extraData; }
    public void setExtraData(String s) { this.extraData = s; }
}
