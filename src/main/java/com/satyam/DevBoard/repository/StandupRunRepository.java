package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.StandupRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StandupRunRepository extends JpaRepository<StandupRun, UUID> {
}
