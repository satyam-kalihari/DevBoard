package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.StandupResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StandupResponseRepository extends JpaRepository<StandupResponse, UUID> {
}
