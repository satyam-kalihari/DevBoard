package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}
