package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateSprintRequest;
import com.satyam.DevBoard.dto.request.UpdateSprintRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Sprint;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthorizationService authorizationService;

//    CREATE SPRINT
    @Transactional
    public Sprint createSprint(CreateSprintRequest request, UUID keycloakId){
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exists"));

        User caller = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not there"));

        boolean isLeader = caller.getId() == project.getLeadUser().getId();

        if(!isLeader){
            throw new AccessDeniedException("Access Denied");
        }

        if (request.getStartDate().isAfter(request.getEndDate())){
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        if(sprintRepository.existsByProjectIdAndName(request.getProjectId(), request.getName())){
            throw new DuplicateResourceException("A sprint with this name already exists in this project");
        }

        Sprint sprint = new Sprint();
        sprint.setProject(project);
        sprint.setName(request.getName());
        sprint.setGoal(request.getGoal());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());

        return sprintRepository.save(sprint);

    }

//    GET ALL SPRINT BY PROJECT ID
    @Transactional(readOnly = true)
    public List<Sprint> getAllByProjectIdWithDetails(UUID id, UUID keycloakId){
        User caller = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exists."));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exists"));

        authorizationService.requiredRole(project.getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.EDITOR);
        if (!projectMemberRepository.existsByProjectIdAndUserId(id, caller.getId())){
            throw new AccessDeniedException("Access Denied");
        }
        return sprintRepository.findAllByProjectIdWithDetails(id);
    }

//    GET SPRINT BY ID
    @Transactional(readOnly = true)
    public Sprint getSprintWithDetails(UUID id){
        return sprintRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint does not exists"));
    }

//    UPDATE SPRINT
    @Transactional
    public Sprint updateSprint(UUID id, UpdateSprintRequest request, UUID keycloakId){

        Sprint sprint = sprintRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint does not exists"));

        User caller = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exists"));

        boolean isLeader = caller.getId() == sprint.getProject().getLeadUser().getId();
        if(!isLeader){
            throw new AccessDeniedException("Access Denied");
        }

        if (request.getName() != null){
            sprint.setName(request.getName());
        }
        if (request.getGoal() != null){
            sprint.setGoal(request.getGoal());
        }
        if (request.getEndDate() != null){
            sprint.setEndDate(request.getEndDate());
        }
        if (request.getStatus() != null){
            sprint.setStatus(request.getStatus());
        }

        return sprintRepository.save(sprint);
    }

//    DELETE SPRINT
    @Transactional
    public void deleteSprint(UUID id, UUID keycloakId){
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint does not exists"));

        User caller = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exists"));

        boolean isLeader = caller.getId() == sprint.getProject().getLeadUser().getId();
        if(!isLeader){
            throw new AccessDeniedException("Access Denied");
        }
        sprintRepository.delete(sprint);
    }

}
