package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Standup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StandupRepository extends JpaRepository<Standup, UUID> {
}
