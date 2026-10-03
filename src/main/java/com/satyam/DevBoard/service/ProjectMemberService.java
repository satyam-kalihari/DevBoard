package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateProjectMemberRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.ProjectMember;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.ProjectMemberRepository;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public ProjectMember createProjectMember(CreateProjectMemberRequest request, UUID keycloakId){

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        authorizationService.requiredRole(project.getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.MEMBER, OrgMember.Role.EDITOR);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);

        return projectMemberRepository.save(projectMember);
    }

//  GET ALL MEMBERS OF THE PROJECT
    @Transactional(readOnly = true)
    public List<ProjectMember> getAllMemberByProjectId(UUID proId){
        return projectMemberRepository.getAllByProjectIdWithDetails(proId);
    }

//    GET ALL PROJECTS OF A USER
    @Transactional(readOnly = true)
    public List<ProjectMember> getAllProjectByUserId(UUID usId){
        return projectMemberRepository.getAllByUserIdWithDetails(usId);
    }

//    GET THE PROJECT MEMBER BY ID
    @Transactional(readOnly = true)
    public ProjectMember getProjectMemberById(UUID id){
        return projectMemberRepository.getByIdWithDetails(id);
    }

    @Transactional
    public void deleteProjectMember(UUID id, UUID keycloakId){
        ProjectMember member = projectMemberRepository.getByIdWithOrg(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project Member does not exists"));

        User caller = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exists"));

        User leader = member.getProject().getLeadUser();

        boolean isLeader = leader != null
                && caller.getId() == leader.getId();

        boolean deletingLeader = leader != null
                && member.getUser().getId() == leader.getId();

        if (deletingLeader){
            authorizationService.requiredRole(member.getProject().getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.EDITOR);
        } else if (!isLeader) {
            authorizationService.requiredRole(member.getProject().getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.EDITOR);
        }

        projectMemberRepository.delete(member);
    }
}
