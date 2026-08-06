package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateTaskRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.*;
import com.satyam.DevBoard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final LabelRepository labelRepository;

    public Task createTask(CreateTaskRequest request){

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("The resource you are trying to fetch does not exist"));

        Sprint sprint = null;
        if (request.getSprintId() != null) {
            sprint = sprintRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new ResourceNotFoundException("The resource you are trying to fetch does not exist"));
        }

        Task parentTask = null;
        if (request.getParentTaskId() != null){
            parentTask = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("The resource you are trying to fetch does not exist"));
        }

        Integer maxRank = taskRepository.findMaxRankByProjectIdAndSprintId(
                request.getProjectId(), request.getSprintId()
        );

        Task task = new Task();
        task.setProject(project);
        task.setSprint(sprint);
        task.setParentTask(parentTask);
        task.setRank(maxRank == null ? 0 : maxRank + 1);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(Task.Status.BACKLOG);
        task.setStoryPoints(request.getStoryPoints());
        task.setDueDate(request.getDueDate());

        List<User> assignees = new ArrayList<>();
        if (request.getAssigneesIds() != null && !request.getAssigneesIds().isEmpty()){
            assignees = userRepository.findAllById(request.getAssigneesIds());

            if(assignees.size() != request.getAssigneesIds().size()){
                throw new ResourceNotFoundException("One or more assignees not found");
            }
        }

        if (request.getLabelIds() != null && !request.getLabelIds().isEmpty()) {
            List<Label> labels = labelRepository.findAllById(request.getLabelIds());
            task.setLabels(labels);
        }

        task.setAssignees(assignees);

        Task save = taskRepository.save(task);
        for (User assignee : assignees){
            notificationService.notifyTaskAssigned(save, assignee);
        }

        return save;
    }
}
