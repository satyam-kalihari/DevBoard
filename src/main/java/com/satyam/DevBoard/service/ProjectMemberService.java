package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateProjectMemberRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.ProjectMember;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.ProjectMemberRepository;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMember createProjectMember(CreateProjectMemberRequest request){
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);

        return projectMemberRepository.save(projectMember);
    }

//  GET ALL MEMBERS OF THE PROJECT
    public List<ProjectMember> getAllMemberByProjectId(UUID proId){
        return projectMemberRepository.getAllByProjectIdWithDetails(proId);
    }

//    GET ALL PROJECTS OF A USER
    public List<ProjectMember> getAllProjectByUserId(UUID usId){
        return projectMemberRepository.getAllByUserIdWithDetails(usId);
    }

//    GET THE PROJECT MEMBER BY ID
    public ProjectMember getProjectMemberById(UUID id){
        return projectMemberRepository.getByIdWithDetails(id);
    }

    public void deleteProjectMember(UUID id){
        ProjectMember member = getProjectMemberById(id);
        projectMemberRepository.delete(member);
    }
}
