package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SprintRepository extends JpaRepository<Sprint, UUID> {
}
