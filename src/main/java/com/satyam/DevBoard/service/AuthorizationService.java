package com.satyam.DevBoard.service;

import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.repository.OrgMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final OrgMemberRepository orgMemberRepository;

    public void requiredRole(UUID orgId, UUID keycloakId, OrgMember.Role... allowedRole){
        OrgMember membership = orgMemberRepository.findByOrganizationIdAndKeycloakId(orgId, keycloakId)
                .orElseThrow(() -> new AccessDeniedException("Not a member of this organization"));

        boolean allowed = Arrays.asList(allowedRole).contains(membership.getRole());

        if (!allowed) {
            throw new AccessDeniedException("Insufficient permissions for this action");
        }
    }
}
