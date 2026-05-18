package ca.sd.portail.dto;

import java.time.OffsetDateTime;

import ca.sd.portail.entity.AgentPlan;

/**
 * DTO de réponse représentant un plan d'exécution d'agents avec son nombre de hits.
 */
public class AgentPlanResponse {

    private String planId;
    private String objective;
    private String status;
    private Integer totalTasks;
    private Integer completedTasks;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private long hitCount;

    public AgentPlanResponse(String planId, String objective, String status,
                              Integer totalTasks, Integer completedTasks,
                              OffsetDateTime startedAt, OffsetDateTime completedAt,
                              long hitCount) {
        this.planId = planId;
        this.objective = objective;
        this.status = status;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.hitCount = hitCount;
    }

    public static AgentPlanResponse from(AgentPlan plan, long hitCount) {
        return new AgentPlanResponse(
            plan.getPlanId(),
            plan.getObjective(),
            plan.getStatus(),
            plan.getTotalTasks(),
            plan.getCompletedTasks(),
            plan.getStartedAt(),
            plan.getCompletedAt(),
            hitCount
        );
    }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTotalTasks() { return totalTasks; }
    public void setTotalTasks(Integer totalTasks) { this.totalTasks = totalTasks; }

    public Integer getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Integer completedTasks) { this.completedTasks = completedTasks; }

    public OffsetDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(OffsetDateTime startedAt) { this.startedAt = startedAt; }

    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }

    public long getHitCount() { return hitCount; }
    public void setHitCount(long hitCount) { this.hitCount = hitCount; }
}
