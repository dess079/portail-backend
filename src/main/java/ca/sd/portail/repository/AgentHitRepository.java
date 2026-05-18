package ca.sd.portail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.entity.AgentHit;

/**
 * Repository JPA pour les activités d'agents (hits).
 */
@Repository
public interface AgentHitRepository extends JpaRepository<AgentHit, Long> {

    List<AgentHit> findByPlanIdOrderByCreatedAtDesc(String planId);

    List<AgentHit> findTop50ByOrderByCreatedAtDesc();

    @Query("SELECT h.agentName as agentName, COUNT(h) as hitCount, AVG(h.durationSeconds) as avgDurationSeconds " +
           "FROM AgentHit h WHERE h.status = 'completed' " +
           "GROUP BY h.agentName ORDER BY COUNT(h) DESC")
    List<AgentHitStats> countByAgentGrouped();

    @Query("SELECT h.skillName as skillName, COUNT(h) as hitCount " +
           "FROM AgentHit h WHERE h.skillName IS NOT NULL AND h.status = 'completed' " +
           "GROUP BY h.skillName ORDER BY COUNT(h) DESC")
    List<SkillHitStats> countBySkillGrouped();

    @Modifying
    @Transactional
    @Query("DELETE FROM AgentHit h WHERE h.planId = :planId")
    void deleteByPlanId(@Param("planId") String planId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AgentHit h WHERE h.planId IN :planIds")
    void deleteByPlanIdIn(@Param("planIds") List<String> planIds);

    /**
     * Projection pour les statistiques par agent.
     */
    interface AgentHitStats {
        String getAgentName();
        Long getHitCount();
        Double getAvgDurationSeconds();
    }

    /**
     * Projection pour les statistiques par skill.
     */
    interface SkillHitStats {
        String getSkillName();
        Long getHitCount();
    }
}
