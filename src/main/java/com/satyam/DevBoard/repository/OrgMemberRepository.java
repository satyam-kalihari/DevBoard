package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.OrgMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrgMemberRepository extends JpaRepository<OrgMember, UUID> {
    @Query("SELECT om FROM OrgMember om " +
            "JOIN FETCH om.organization " +
            "JOIN FETCH om.user")
    List<OrgMember> findAllWithDetails();

    @Query("SELECT om FROM OrgMember om " +
            "JOIN FETCH om.organization " +
            "JOIN FETCH om.user " +
            "WHERE om.id = :id")
    Optional<OrgMember> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT om FROM OrgMember om " +
            "JOIN FETCH om.organization " +
            "JOIN FETCH om.user " +
            "WHERE om.organization.id = :orgId")
    List<OrgMember> findAllByOrgId(@Param("orgId") UUID orgId);

    boolean existsByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
