package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateOrgMemberRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.OrgMemberRepository;
import com.satyam.DevBoard.repository.OrganizationRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrgMemberService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrgMemberRepository orgMemberRepository;

    public OrgMember createOrgMember(CreateOrgMemberRequest request){
        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not in the database."));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User cannot be found."));

        OrgMember orgMember = new OrgMember();
        orgMember.setOrgId(organization);
        orgMember.setUserId(user);
        orgMember.setRole(request.getRole());

        return orgMemberRepository.save(orgMember);
    }
}
