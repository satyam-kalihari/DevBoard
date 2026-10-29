package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Standup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StandupRepository extends JpaRepository<Standup, UUID> {
    @Query(
            "select su from Standup su " +
                    "join fetch su.project " +
                    "where su.project.id = :projectId"
    )
    Optional<Standup> findStandupByProjectId(@Param("projectId") UUID projectId);

    @Query("select su from Standup  su " +
            "join fetch su.project " +
            "where su.id = :id")
    Optional<Standup> findStandupByIdWithDetails(@Param("id") UUID id);

    boolean existsByProjectId(UUID projectId);

    @Query("select s from Standup s join fetch s.project p join fetch p.organization " +
            "where s.isActive = true and p.status = :status")
    List<Standup> findAllActiveByProjectStatus(@Param("status") Project.Status status);

    @Query("select s from Standup s join fetch s.project p join fetch p.organization " +
            "where s.id = :id")
    Optional<Standup> findByIdWithProject(@Param("id") UUID id);
}
