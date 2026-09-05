package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateOrgMemberRequest;
import com.satyam.DevBoard.dto.request.UpdateOrgMemberRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.OrgMemberRepository;
import com.satyam.DevBoard.repository.OrganizationRepository;
import com.satyam.DevBoard.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrgMemberService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrgMemberRepository orgMemberRepository;

    @Transactional
    public OrgMember createOrgMember(CreateOrgMemberRequest request){
        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not in the database."));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User cannot be found."));

        if (orgMemberRepository.existsByOrganizationIdAndUserId(request.getOrgId(), request.getUserId())) {
            throw new DuplicateResourceException("User is already a member of this organization");
        }

        OrgMember orgMember = new OrgMember();
        orgMember.setOrganization(organization);
        orgMember.setUser(user);
        orgMember.setRole(request.getRole());

        return orgMemberRepository.save(orgMember);
    }

    @Transactional(readOnly = true)
    public OrgMember getOrgMemberById(UUID id){
        OrgMember member = orgMemberRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("This Organization member does not exist"));
        return member;
    }

    @Transactional(readOnly = true)
    public List<OrgMember> getAllOrgMember(){
        List<OrgMember> orgMembers = orgMemberRepository.findAllWithDetails();
        return orgMembers;
    }

    @Transactional(readOnly = true)
    public List<OrgMember> getOrgMembersByOrg(UUID orgId) {
        return orgMemberRepository.findAllByOrgId(orgId);
    }

    @Transactional
    public OrgMember updateOrgMember(UUID id, UpdateOrgMemberRequest request){
        OrgMember orgMember = orgMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization member not found"));

        if(request.getRole() != null){
            orgMember.setRole(request.getRole());
        }

        return orgMemberRepository.save(orgMember);
    }

    @Transactional
    public void deleteOrgMember(UUID id){
        OrgMember member = getOrgMemberById(id);
        orgMemberRepository.delete(member);
    }
}
