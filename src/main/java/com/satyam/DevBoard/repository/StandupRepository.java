package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Standup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
