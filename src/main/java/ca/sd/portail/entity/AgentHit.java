package ca.sd.portail.entity;

import java.time.OffsetDateTime;
import jakarta.persistence.*;

/**
 * Entité JPA représentant une activité d'agent IA (hit).
 * Mappée sur la table TB_AGT_HIT.
 */
@Entity
@Table(name = "TB_AGT_HIT")
public class AgentHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "plan_id", length = 255)
    private String planId;

    @Column(name = "task_id", length = 255)
    private String taskId;

    @Column(name = "agent_name", length = 255)
    private String agentName;

    @Column(name = "skill_name", length = 255)
    private String skillName;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "wave")
    private Integer wave;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public AgentHit() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getWave() { return wave; }
    public void setWave(Integer wave) { this.wave = wave; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
