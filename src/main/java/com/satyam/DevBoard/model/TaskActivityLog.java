package com.satyam.DevBoard.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name = "task_activity_log")
public class TaskActivityLog {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "before_state", columnDefinition = "jsonb")
    private Map<String, Object> beforeState;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "after_state", columnDefinition = "jsonb")
    private Map<String, Object> afterState;

    @Column(name = "occurred_at", updatable = false, nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();

    // Action type constants — used by services when logging events
    // Avoids magic strings scattered across the codebase
    public static final String ACTION_TASK_CREATED     = "TASK_CREATED";
    public static final String ACTION_STATUS_CHANGED   = "STATUS_CHANGED";
    public static final String ACTION_PRIORITY_CHANGED = "PRIORITY_CHANGED";
    public static final String ACTION_ASSIGNEE_ADDED   = "ASSIGNEE_ADDED";
    public static final String ACTION_ASSIGNEE_REMOVED = "ASSIGNEE_REMOVED";
    public static final String ACTION_SPRINT_CHANGED   = "SPRINT_CHANGED";
    public static final String ACTION_COMMENT_ADDED    = "COMMENT_ADDED";
    public static final String ACTION_DUE_DATE_CHANGED = "DUE_DATE_CHANGED";
    public static final String ACTION_LABEL_ADDED      = "LABEL_ADDED";
    public static final String ACTION_LABEL_REMOVED    = "LABEL_REMOVED";
}