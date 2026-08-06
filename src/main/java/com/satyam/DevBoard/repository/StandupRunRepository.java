package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.StandupRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface StandupRunRepository extends JpaRepository<StandupRun, UUID> {
    boolean existsByStandupIdAndRunDate(UUID standupId, LocalDate runDate);
}
