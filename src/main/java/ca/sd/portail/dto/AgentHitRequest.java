package ca.sd.portail.dto;

/**
 * DTO pour la création d'un hit d'activité d'agent.
 * Le champ planObjective n'est utilisé que lors du premier hit pour créer le plan s'il n'existe pas.
 */
public class AgentHitRequest {

    private String planId;
    private String taskId;
    private String agentName;
    private String skillName;
    private String status;
    private String description;
    private Integer wave;
    private Integer durationSeconds;
    private String planObjective;
    /** Statut du plan lui-même (ex. "completed", "failed") — optionnel. */
    private String planStatus;

    public AgentHitRequest() {
    }

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

    public String getPlanObjective() { return planObjective; }
    public void setPlanObjective(String planObjective) { this.planObjective = planObjective; }

    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
}
