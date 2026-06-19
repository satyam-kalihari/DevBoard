package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.OrgMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrgMemberRepository extends JpaRepository<OrgMember, UUID> {
}
