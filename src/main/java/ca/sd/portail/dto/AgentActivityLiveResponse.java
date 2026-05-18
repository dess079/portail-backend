package ca.sd.portail.dto;

import java.util.List;

import ca.sd.portail.entity.AgentHit;

/**
 * DTO pour la vue live de l'activité des agents.
 */
public class AgentActivityLiveResponse {

    private List<AgentHit> recentHits;
    private String activePlanId;
    private String activePlanObjective;
    private int activeAgentsCount;

    public AgentActivityLiveResponse() {
    }

    public AgentActivityLiveResponse(List<AgentHit> recentHits, String activePlanId,
                                     String activePlanObjective, int activeAgentsCount) {
        this.recentHits = recentHits;
        this.activePlanId = activePlanId;
        this.activePlanObjective = activePlanObjective;
        this.activeAgentsCount = activeAgentsCount;
    }

    public List<AgentHit> getRecentHits() { return recentHits; }
    public void setRecentHits(List<AgentHit> recentHits) { this.recentHits = recentHits; }

    public String getActivePlanId() { return activePlanId; }
    public void setActivePlanId(String activePlanId) { this.activePlanId = activePlanId; }

    public String getActivePlanObjective() { return activePlanObjective; }
    public void setActivePlanObjective(String activePlanObjective) { this.activePlanObjective = activePlanObjective; }

    public int getActiveAgentsCount() { return activeAgentsCount; }
    public void setActiveAgentsCount(int activeAgentsCount) { this.activeAgentsCount = activeAgentsCount; }
}
