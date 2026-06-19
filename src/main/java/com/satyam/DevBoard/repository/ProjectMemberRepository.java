package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {
}
