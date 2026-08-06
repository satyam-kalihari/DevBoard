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
        projectMember.setProjectId(project);
        projectMember.setUserId(user);

        return projectMemberRepository.save(projectMember);
    }
}
