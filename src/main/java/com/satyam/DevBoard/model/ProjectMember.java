package com.satyam.DevBoard.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "project_members")
public class ProjectMember {

    @Id
    @Setter(AccessLevel.NONE)
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false, nullable = false)
    private LocalDateTime joinedAt;
}
