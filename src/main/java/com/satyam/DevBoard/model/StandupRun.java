package com.satyam.DevBoard.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "standup_runs",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"standup_id", "run_date"}
        )
)
public class StandupRun {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standup_id", nullable = false)
    private Standup standup;

    @Column(name = "run_date", nullable = false)
    private LocalDate runDate;

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    @Column(name = "is_finalized", nullable = false)
    private boolean isFinalized = false;

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @OneToMany(mappedBy = "standupRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StandupResponse> responses = new ArrayList<>();
}