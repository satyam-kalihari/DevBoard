package com.satyam.DevBoard.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization orgId;

    @OneToOne
    @JoinColumn(name = "lead_user_id", nullable = false)
    private User leadUserId;

    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "target_date", nullable = false)
    private LocalDateTime targetDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum Status{
        ACTIVE, PENDING, COMPLETED
    }
}
