package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, UUID> {

    @Query("SELECT tc FROM TaskComment tc " +
            "JOIN FETCH tc.task " +
            "JOIN FETCH tc.user " +
            "WHERE tc.id = :id")
    Optional<TaskComment> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT tc FROM TaskComment tc " +
            "JOIN FETCH tc.task " +
            "JOIN FETCH tc.user " +
            "WHERE tc.task.id = :taskId " +
            "ORDER BY tc.createdAt ASC")
    List<TaskComment> findAllByTaskIdWithDetails(@Param("taskId") UUID taskId);
}