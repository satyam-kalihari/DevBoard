package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label, UUID> {
    @Query(
            "SELECT la FROM Label la " + "JOIN FETCH la.organization " + "WHERE la.organization.id = :orgId"
    )
    List<Label> getAllLabelByOrgId(@Param("orgId") UUID orgId);

    @Query(
            "SELECT la FROM Label la " + "JOIN FETCH la.organization " + "WHERE la.id = :id"
    )
    Optional<Label> getLabelByIdWithDetail(@Param("id") UUID id);

    @Query(
            "SELECT la FROM Label la " + "JOIN FETCH la.organization"
    )
    List<Label> getAllLabelWithDetail();
}
