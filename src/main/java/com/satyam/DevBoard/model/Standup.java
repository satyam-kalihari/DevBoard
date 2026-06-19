package com.satyam.DevBoard.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "standups")
public class Standup {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, unique = true)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project projectId;

    @Column(name = "schedule_days", nullable = false)
    private String scheduleDays = "MON,TUE,WED,THU,FRI";

    @Column(name = "scheduled_time", nullable = false)
    private LocalTime scheduledTime = LocalTime.of(9, 0);

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "standup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StandupRun> runs = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
