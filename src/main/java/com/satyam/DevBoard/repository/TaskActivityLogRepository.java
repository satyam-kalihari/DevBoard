package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.TaskActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskActivityLogRepository extends JpaRepository<TaskActivityLog, UUID> {

    @Query("SELECT tal FROM TaskActivityLog tal " +
            "JOIN FETCH tal.task " +
            "JOIN FETCH tal.actor " +
            "WHERE tal.task.id = :taskId " +
            "ORDER BY tal.occurredAt ASC")
    List<TaskActivityLog> findAllByTaskIdWithDetails(@Param("taskId") UUID taskId);

    @Query("SELECT tal FROM TaskActivityLog tal " +
            "JOIN FETCH tal.task " +
            "JOIN FETCH tal.actor " +
            "WHERE tal.actor.id = :actorId " +
            "ORDER BY tal.occurredAt DESC")
    List<TaskActivityLog> findAllByActorIdWithDetails(@Param("actorId") UUID actorId);
}