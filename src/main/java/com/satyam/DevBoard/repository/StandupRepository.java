package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Standup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StandupRepository extends JpaRepository<Standup, UUID> {
}
