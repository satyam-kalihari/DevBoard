package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query(
            "SELECT pro FROM Project pro " + "JOIN FETCH pro.organization " + "JOIN FETCH pro.leadUser"
    )
    List<Project> findAllWithDetails();

    @Query(
            "SELECT pro FROM Project pro " + "JOIN FETCH pro.organization " + "JOIN FETCH pro.leadUser " +
                    "WHERE pro.id = :id"
    )
    Project findByIdWithDetail(@Param("id") UUID id);

    @Query(
            "SELECT pro FROM Project pro " + "JOIN FETCH pro.organization " + "JOIN FETCH pro.leadUser " +
                    "where pro.organization.id = :orgId"
    )
    List<Project> findProjectWithOrganizationId(@Param("orgId") UUID orgId);

}
