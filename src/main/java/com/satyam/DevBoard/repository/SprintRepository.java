package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, UUID> {
    boolean existsByProjectIdAndName(UUID projectId, String name);
}
