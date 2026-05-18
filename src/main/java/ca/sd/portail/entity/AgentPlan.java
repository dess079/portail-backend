package ca.sd.portail.entity;

import java.time.OffsetDateTime;
import jakarta.persistence.*;

/**
 * Entité JPA représentant un plan d'exécution d'agent IA.
 * Mappée sur la table TB_AGT_PLAN.
 */
@Entity
@Table(name = "TB_AGT_PLAN")
public class AgentPlan {

    @Id
    @Column(name = "plan_id", nullable = false, length = 255)
    private String planId;

    @Column(name = "objective", columnDefinition = "TEXT")
    private String objective;

    @Column(name = "total_tasks")
    private Integer totalTasks;

    @Column(name = "completed_tasks")
    private Integer completedTasks;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    public AgentPlan() {
    }

    public AgentPlan(String planId, String objective) {
        this.planId = planId;
        this.objective = objective;
        this.totalTasks = 0;
        this.completedTasks = 0;
        this.status = "running";
        this.startedAt = OffsetDateTime.now();
    }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }

    public Integer getTotalTasks() { return totalTasks; }
    public void setTotalTasks(Integer totalTasks) { this.totalTasks = totalTasks; }

    public Integer getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Integer completedTasks) { this.completedTasks = completedTasks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public OffsetDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(OffsetDateTime startedAt) { this.startedAt = startedAt; }

    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }
}
