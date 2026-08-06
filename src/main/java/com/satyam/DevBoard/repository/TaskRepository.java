package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Integer findMaxRankByProjectIdAndSprintId(UUID projectId, UUID sprintId);

    @Query("SELECT MAX(t.rank) FROM Task t WHERE t.project.id = :projectId " +
            "AND (:sprintId IS NULL AND t.sprint IS NULL OR t.sprint.id = :sprintId)")
    Integer findMaxByProjectIdAndSprintId(@Param("projectId") UUID projectId, @Param("sprintId") UUID sprintId);
}
