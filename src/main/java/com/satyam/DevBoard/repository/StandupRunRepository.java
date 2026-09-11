package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.StandupRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StandupRunRepository extends JpaRepository<StandupRun, UUID> {
    @Query(
            "select sur from StandupRun sur " +
                    "join fetch sur.standup " +
                    "left join fetch sur.responses " +
                    "where sur.standup.id = :standupId"
    )
    List<StandupRun> findAllByStandupIdWithDetails(@Param("standupId") UUID standupId);

    @Query(
            "select sur from StandupRun sur " +
                    "join fetch sur.standup " +
                    "left join fetch sur.responses " +
                    "where sur.id = :id"
    )
    Optional<StandupRun> findByIdWithDetails(@Param("id") UUID id);

    boolean existsByStandupIdAndRunDate(UUID standupId, LocalDate runDate);

//    UUID id(UUID id);
}
