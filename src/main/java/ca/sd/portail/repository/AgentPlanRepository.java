package ca.sd.portail.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.sd.portail.entity.AgentPlan;

/**
 * Repository JPA pour les plans d'exécution d'agents.
 */
@Repository
public interface AgentPlanRepository extends JpaRepository<AgentPlan, String> {

    Page<AgentPlan> findAllByOrderByStartedAtDesc(Pageable pageable);

    @Query("SELECT p FROM AgentPlan p WHERE " +
           "(:planId IS NULL OR LOWER(p.planId) LIKE LOWER(CONCAT('%', :planId, '%'))) AND " +
           "(:objective IS NULL OR LOWER(p.objective) LIKE LOWER(CONCAT('%', :objective, '%'))) AND " +
           "(:status IS NULL OR p.status = :status) " +
           "ORDER BY p.startedAt DESC")
    Page<AgentPlan> findAllWithFilters(
            @Param("planId") String planId,
            @Param("objective") String objective,
            @Param("status") String status,
            Pageable pageable);

    /**
     * Recherche globale LIKE sur planId OU objective (insensible à la casse).
     *
     * @param search terme de recherche
     * @param pageable pagination
     * @return page de plans correspondants
     */
    @Query("SELECT p FROM AgentPlan p WHERE " +
           "LOWER(p.planId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.objective) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "ORDER BY p.startedAt DESC")
    Page<AgentPlan> findAllWithSearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(h) FROM AgentHit h WHERE h.planId = :planId")
    long countHitsByPlanId(@Param("planId") String planId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AgentPlan p WHERE p.planId IN :planIds")
    void deleteByPlanIdIn(@Param("planIds") List<String> planIds);
}
