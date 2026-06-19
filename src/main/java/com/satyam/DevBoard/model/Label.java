package com.satyam.DevBoard.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "labels", uniqueConstraints = @UniqueConstraint(
        columnNames = {"org_id", "name"}
))
public class Label {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "color_hex", nullable = false, length = 7)
    private String colorHex = "#6366F1";
}
