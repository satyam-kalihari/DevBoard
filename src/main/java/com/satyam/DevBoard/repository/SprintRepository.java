package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, UUID> {

    @Query(
            "SELECT s FROM Sprint s " +
                    "JOIN FETCH s.project " +
                    "LEFT JOIN FETCH s.tasks " +
                    "WHERE s.id = :id"
    )
    Optional<Sprint> findByIdWithDetails(@Param("id") UUID id);

    @Query(
            "SELECT s FROM Sprint s " +
                    "JOIN FETCH s.project " +
                    "LEFT JOIN FETCH s.tasks " +
                    "WHERE s.project.id = :projectId"
    )
    List<Sprint> findAllByProjectIdWithDetails(@Param("projectId") UUID projectId);

    boolean existsByProjectIdAndName(UUID projectId, String name);
}
