package ca.sd.portail.dto;

import java.util.List;

import ca.sd.portail.repository.AgentHitRepository.AgentHitStats;
import ca.sd.portail.repository.AgentHitRepository.SkillHitStats;

/**
 * DTO pour les statistiques agrégées de l'activité des agents.
 */
public class AgentActivityStatsResponse {

    private List<AgentHitStats> topAgents;
    private List<SkillHitStats> topSkills;
    private long totalHits;
    private long totalPlans;

    public AgentActivityStatsResponse() {
    }

    public AgentActivityStatsResponse(List<AgentHitStats> topAgents, List<SkillHitStats> topSkills,
                                      long totalHits, long totalPlans) {
        this.topAgents = topAgents;
        this.topSkills = topSkills;
        this.totalHits = totalHits;
        this.totalPlans = totalPlans;
    }

    public List<AgentHitStats> getTopAgents() { return topAgents; }
    public void setTopAgents(List<AgentHitStats> topAgents) { this.topAgents = topAgents; }

    public List<SkillHitStats> getTopSkills() { return topSkills; }
    public void setTopSkills(List<SkillHitStats> topSkills) { this.topSkills = topSkills; }

    public long getTotalHits() { return totalHits; }
    public void setTotalHits(long totalHits) { this.totalHits = totalHits; }

    public long getTotalPlans() { return totalPlans; }
    public void setTotalPlans(long totalPlans) { this.totalPlans = totalPlans; }
}
