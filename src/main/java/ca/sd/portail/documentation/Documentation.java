package ca.sd.portail.documentation;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_DOC_DOCUMENTATION",
       uniqueConstraints = @UniqueConstraint(columnNames = {"screen_key", "application_code"}))
public class Documentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;

    @Column(name = "screen_key", nullable = false)
    private String screenKey;

    @Column(name = "application_code", nullable = false)
    private String applicationCode;

    @Column(name = "title_fr", nullable = false)
    private String titleFr;

    @Column(name = "title_en", nullable = false)
    private String titleEn;

    @Column(name = "subtitle_fr")
    private String subtitleFr;

    @Column(name = "subtitle_en")
    private String subtitleEn;

    @Column(name = "icon_name")
    private String iconName;

    @Column(name = "version", nullable = false)
    private int version = 1;

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

    @OneToMany(mappedBy = "documentation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<DocumentationSection> sections = new ArrayList<>();

    // Getters and setters
    public Long getDocId() { return docId; }
    public String getScreenKey() { return screenKey; }
    public void setScreenKey(String screenKey) { this.screenKey = screenKey; }
    public String getApplicationCode() { return applicationCode; }
    public void setApplicationCode(String applicationCode) { this.applicationCode = applicationCode; }
    public String getTitleFr() { return titleFr; }
    public void setTitleFr(String titleFr) { this.titleFr = titleFr; }
    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
    public String getSubtitleFr() { return subtitleFr; }
    public void setSubtitleFr(String subtitleFr) { this.subtitleFr = subtitleFr; }
    public String getSubtitleEn() { return subtitleEn; }
    public void setSubtitleEn(String subtitleEn) { this.subtitleEn = subtitleEn; }
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }
    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
    public List<DocumentationSection> getSections() { return sections; }
}
