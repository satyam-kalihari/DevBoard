package com.satyam.DevBoard.service;

import com.satyam.DevBoard.dto.request.CreateTaskRequest;
import com.satyam.DevBoard.dto.request.UpdateTaskRequest;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.*;
import com.satyam.DevBoard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final LabelRepository labelRepository;

//    CREATE TASK
    @Transactional
    public Task createTask(CreateTaskRequest request){

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("The project you are trying to fetch does not exist"));

        Sprint sprint = null;
        if (request.getSprintId() != null) {
            sprint = sprintRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new ResourceNotFoundException("The sprint you are trying to fetch does not exist"));
        }

        Task parentTask = null;
        if (request.getParentTaskId() != null){
            parentTask = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("The parent task you are trying to fetch does not exist"));
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
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(request.getLabelIds()));
            task.setLabels(labels);
        }

        task.setAssignees(assignees);

        Task save = taskRepository.save(task);
        for (User assignee : assignees){
            notificationService.notifyTaskAssigned(save, assignee);
        }

        return save;
    }

//    GET BY TASK ID
    @Transactional(readOnly = true)
    public Task getByIdWithDetails(UUID id){
        return taskRepository.getByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task does not exists"));
    }

//    GET ALL TASK BY PROJECT ID
    @Transactional(readOnly = true)
    public List<Task> getAllByProjectIdWithDetails(UUID projectId){
        return taskRepository.getAllByProjectIdWithDetails(projectId);
    }

//    GET ALL TASK BY SPRINT ID
    @Transactional(readOnly = true)
    public List<Task> getAllBySprintIdWithDetails( UUID sprintId){
        return taskRepository.getAllBySprintIdWithDetails(sprintId);
    }

//    GET ALL BACKLOG TASK BY PROJECT ID
    @Transactional(readOnly = true)
    public List<Task> getBacklogTaskByProjectId(UUID projectId){
        return taskRepository.getBacklogTaskByProjectIdWithDetails(projectId);
    }

//    UPDATE TASK
    @Transactional
    public Task updateTask(UUID id, UpdateTaskRequest request){
        Task task = taskRepository.getByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("This task does not exists"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getStoryPoints() != null) task.setStoryPoints(request.getStoryPoints());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        if (request.getSprintId() != null) {
            Sprint sprint = sprintRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sprint not found."));
            task.setSprint(sprint);
        }
        if (request.getParentTaskId() != null) {
            Task parentTask = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent task not found."));
            task.setParentTask(parentTask);
        }

        if (request.getAssigneeIds() != null){
            List<User> users = userRepository.findAllById(request.getAssigneeIds());
            task.getAssignees().clear();
            task.getAssignees().addAll(users);
        }

        if (request.getLabelIds() != null){
            List<Label> labels = labelRepository.findAllById(request.getLabelIds());
            task.getLabels().clear();
            task.getLabels().addAll(labels);
        }

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
        taskRepository.delete(task);
    }

}
