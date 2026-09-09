package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateSprintRequest;
import com.satyam.DevBoard.dto.request.UpdateSprintRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;

//    CREATE SPRINT
    @Transactional
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

//    GET ALL SPRINT BY PROJECT ID
    @Transactional(readOnly = true)
    public List<Sprint> getAllByProjectIdWithDetails(UUID id){
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
    public Sprint updateSprint(UUID id, UpdateSprintRequest request){
        Sprint sprint = getSprintWithDetails(id);

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
    public void deleteSprint(UUID id){
        Sprint sprint = getSprintWithDetails(id);
        sprintRepository.delete(sprint);
    }

}
