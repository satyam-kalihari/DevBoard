package com.satyam.DevBoard.model;

import jakarta.persistence.*;
        import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "standup_responses",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"standup_run_id", "user_id"}
        )
)
public class StandupResponse {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standup_run_id", nullable = false)
    private StandupRun standupRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "answer_yesterday", columnDefinition = "TEXT")
    private String answerYesterday;

    @Column(name = "answer_today", columnDefinition = "TEXT")
    private String answerToday;

    @Column(name = "answer_blockers", columnDefinition = "TEXT")
    private String answerBlockers;

    @Column(name = "has_blockers", nullable = false)
    private boolean hasBlockers = false;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false, nullable = false)
    private LocalDateTime submittedAt;
}