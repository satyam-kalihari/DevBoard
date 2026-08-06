package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateSprintRequest;
import com.satyam.DevBoard.exception.DuplicateResourceException;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Sprint;
import com.satyam.DevBoard.model.Task;
import com.satyam.DevBoard.repository.ProjectRepository;
import com.satyam.DevBoard.repository.SprintRepository;
import com.satyam.DevBoard.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;

    public Sprint createSprint(CreateSprintRequest request){
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exists"));

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
}
