package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateProjectRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.OrganizationRepository;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public Project createProject(UUID orgId, UUID userId, CreateProjectRequest request){

        if (request.getStartDate() != null && request.getTargetDate() != null && request.getStartDate().isAfter(request.getTargetDate()) ){
            throw new IllegalArgumentException("Start date cannot be after target date");
        }

        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        User user = userRepository.findById(userId)
                .orElseThrow( () -> new ResourceNotFoundException("User does not exists"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setEndDate(request.getEndDate());
        project.setStartDate(request.getStartDate());
        project.setTargetDate(request.getTargetDate());
        project.setOrganization(organization);
        project.setLeadUser(user);

        return projectRepository.save(project);
    }
}
