package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.StandupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StandupResponseRepository extends JpaRepository<StandupResponse, UUID> {
    @Query("select sr from StandupResponse sr " +
            "join fetch sr.standupRun " +
            "join fetch  sr.user " +
            "where sr.standupRun.id = :standupRunId")
    List<StandupResponse> findByStandupRunIdWithDetails(@Param("standupRunId") UUID standupRunId);

    @Query("select sr from StandupResponse sr " +
            "join fetch sr.standupRun " +
            "join fetch sr.user " +
            "where sr.user.id = :userId")
    List<StandupResponse> findByUserIdWithDetails(@Param("userId") UUID userId);

    @Query("select sr from StandupResponse sr " +
            "join fetch sr.standupRun " +
            "join fetch sr.user " +
            "where sr.id = :id")
    Optional<StandupResponse> findByIdWithDetails(@Param("id") UUID id);

    boolean existsByStandupRunIdAndUserId(UUID standupRunId, UUID userId);
}
