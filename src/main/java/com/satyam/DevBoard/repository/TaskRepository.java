package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignees", "labels", "labels.organization"})
    @Query("select t from Task t where t.id = :id")
    Optional<Task> getByIdWithDetails(@Param("id") UUID id);

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignees", "labels", "labels.organization"})
    @Query("select t from Task t where t.project.id = :projectId order by t.rank asc , t.createdAt desc ")
    List<Task> getAllByProjectIdWithDetails(@Param("projectId") UUID projectId);

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignees", "labels", "labels.organization"})
    @Query("select t from Task t where t.sprint.id = :sprintId order by t.rank asc, t.createdAt desc")
    List<Task> getAllBySprintIdWithDetails(@Param("sprintId") UUID sprintId);

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignees", "labels", "labels.organization"})
    @Query("select t from Task t where t.project.id = :projectId and t.sprint.id is null order by t.rank asc , t.createdAt desc")
    List<Task> getBacklogTaskByProjectIdWithDetails(@Param("projectId") UUID projectId);

    @Query("select t from Task t join t.assignees u where u.id = :userId")
    List<Task> getAllByAssigneeId(@Param("userId") UUID userId);

    @Query("SELECT MAX(t.rank) FROM Task t WHERE t.project.id = :projectId " +
            "AND (:sprintId IS NULL AND t.sprint IS NULL OR t.sprint.id = :sprintId)")
    Integer findMaxRankByProjectIdAndSprintId(@Param("projectId") UUID projectId, @Param("sprintId") UUID sprintId);
}
