package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateProjectRequest;
import com.satyam.DevBoard.dto.request.UpdateProjectRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.OrganizationRepository;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

//    CREATE PROJECT METHOD
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
//        project.setEndDate(request.getEndDate());
        project.setStartDate(request.getStartDate());
        project.setTargetDate(request.getTargetDate());
        project.setOrganization(organization);
        project.setLeadUser(user);

        return projectRepository.save(project);
    }

//    GET PROJECT BY ID METHOD
    public Project getProjectById(UUID id){
        return projectRepository.findByIdWithDetail(id);
    }

//    GET ALL PROJECTS
    public List<Project> getAllProject(){
        return projectRepository.findAllWithDetails();
    }

//    UPDATE PROJECT
    public Project updateProject(UUID id, UpdateProjectRequest request){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The Project does not exists"));

        if(request.getName() != null){
            project.setName(request.getName());
        }

        if (request.getDescription() != null){
            project.setDescription(request.getDescription());
        }

        if (request.getStatus() != null){
            project.setStatus(request.getStatus());
        }

        if (request.getStartDate() != null){
            if ((request.getTargetDate() != null && request.getStartDate().isAfter(request.getTargetDate()))
                    ||(request.getTargetDate() == null && request.getStartDate().isAfter(project.getTargetDate()))){
                throw new IllegalArgumentException("Start date cannot be after target date");
            }
            project.setTargetDate(request.getTargetDate());
        }

        if (request.getTargetDate() != null){
            project.setTargetDate(request.getTargetDate());
        }

        if(request.getEndDate() != null){
            project.setEndDate(request.getEndDate());
        }

        return projectRepository.save(project);
    }

//    DELETE PROJECT METHOD
    public void deleteProject(UUID id){
        Project project = getProjectById(id);
        projectRepository.delete(project);

    }
}
