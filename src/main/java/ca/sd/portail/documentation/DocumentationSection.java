package ca.sd.portail.documentation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_DOC_SECTION",
       uniqueConstraints = @UniqueConstraint(columnNames = {"doc_id", "section_key"}))
public class DocumentationSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Documentation documentation;

    @Column(name = "section_key", nullable = false)
    private String sectionKey;

    @Column(name = "heading_level", nullable = false)
    private int headingLevel = 2;

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0;

    @Column(name = "title_fr", nullable = false)
    private String titleFr;

    @Column(name = "title_en", nullable = false)
    private String titleEn;

    @Column(name = "content_fr", nullable = false, columnDefinition = "TEXT")
    private String contentFr = "";

    @Column(name = "content_en", nullable = false, columnDefinition = "TEXT")
    private String contentEn = "";

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_by", nullable = false)
    private String createdBy = "system";

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    // Getters and setters
    public Long getSectionId() { return sectionId; }
    public Documentation getDocumentation() { return documentation; }
    public void setDocumentation(Documentation documentation) { this.documentation = documentation; }
    public String getSectionKey() { return sectionKey; }
    public void setSectionKey(String sectionKey) { this.sectionKey = sectionKey; }
    public int getHeadingLevel() { return headingLevel; }
    public void setHeadingLevel(int headingLevel) { this.headingLevel = headingLevel; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public String getTitleFr() { return titleFr; }
    public void setTitleFr(String titleFr) { this.titleFr = titleFr; }
    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
    public String getContentFr() { return contentFr; }
    public void setContentFr(String contentFr) { this.contentFr = contentFr; }
    public String getContentEn() { return contentEn; }
    public void setContentEn(String contentEn) { this.contentEn = contentEn; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime d) { this.lastModifiedDate = d; }
    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String s) { this.lastModifiedBy = s; }
}
