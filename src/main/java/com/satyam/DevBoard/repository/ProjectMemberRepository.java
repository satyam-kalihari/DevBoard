package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    @Query(
            "SELECT pm FROM ProjectMember pm " + " JOIN FETCH pm.project " + " JOIN FETCH pm.user " + " WHERE pm.project.id = :proId"
    )
    List<ProjectMember> getAllByProjectIdWithDetails(@Param("proId") UUID proId);

    @Query(
            "SELECT pm FROM ProjectMember pm " + " JOIN FETCH pm.project " + " JOIN FETCH pm.user " + " WHERE pm.user.id = :usId"
    )
    List<ProjectMember> getAllByUserIdWithDetails(@Param("usId") UUID usId);

    @Query(
            "SELECT pm FROM ProjectMember pm " + " JOIN FETCH pm.project " + " JOIN FETCH pm.user " + " WHERE pm.id = :id"
    )
    ProjectMember getByIdWithDetails(@Param("id") UUID id);

    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);
}
