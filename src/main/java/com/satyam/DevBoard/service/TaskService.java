package com.satyam.DevBoard.service;

import com.satyam.DevBoard.cache.ProjectTasksChanged;
import com.satyam.DevBoard.cache.TaskListCache;
import com.satyam.DevBoard.dto.request.CreateTaskRequest;
import com.satyam.DevBoard.dto.request.UpdateTaskRequest;
import com.satyam.DevBoard.dto.response.TaskResponse;
import com.satyam.DevBoard.event.TaskAssignedEvent;
import com.satyam.DevBoard.event.TaskCreatedEvent;
import com.satyam.DevBoard.event.TaskStatusChangedAuditEvent;
import com.satyam.DevBoard.event.TaskUnassignedEvent;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.*;
import com.satyam.DevBoard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final LabelRepository labelRepository;
    private final TaskActivityLogService taskActivityLogService;
    private final AuthorizationService authorizationService;
    private final ProjectMemberRepository projectMemberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TaskListCache taskListCache;

//    CREATE TASK
    @Transactional
    public Task createTask(CreateTaskRequest request,UUID keycloakId){

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("The project you are trying to fetch does not exist"));

        User actor = authorizationService.requiredRole(project.getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.EDITOR, OrgMember.Role.MEMBER);

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
                throw new ResourceNotFoundException("One or more assignees not found");//future mai kon se assignees nahi hai to vo bhi batana padega
            }
        }

        if (request.getLabelIds() != null && !request.getLabelIds().isEmpty()) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(request.getLabelIds()));
            task.setLabels(labels);
        }

        task.setAssignees(assignees);

        Task save = taskRepository.save(task);

        UUID orgId = project.getOrganization().getId();
        eventPublisher.publishEvent(new TaskCreatedEvent(
                UUID.randomUUID(),
                save.getId(),
                save.getTitle(),
                project.getId(),
                orgId,
                actor.getId(),
                save.getStatus().name(),
                save.getPriority().name(),
                Instant.now()
        ));

        for (User assignee : assignees){
            eventPublisher.publishEvent(
                    new TaskAssignedEvent(
                            UUID.randomUUID(),
                            save.getId(),
                            save.getTitle(),
                            project.getId(),
                            project.getOrganization().getId(),
                            assignee.getId(),
                            null,
                            Instant.now()
                    )
            );
        }


        eventPublisher.publishEvent(new ProjectTasksChanged(project.getId()));
        return save;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getProjectTasks(UUID projectId, UUID keycloakId) {
        UUID orgId = projectRepository.findOrganizationIdById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // 1. Authorize first, on every request, hit or miss
        authorizationService.requiredRole(orgId, keycloakId,
                OrgMember.Role.OWNER, OrgMember.Role.EDITOR, OrgMember.Role.MEMBER, OrgMember.Role.VIEWER);

        // 2. Then the cache, 3. then the database
        return taskListCache.get(projectId).orElseGet(() -> {
            List<TaskResponse> tasks = taskRepository.getAllByProjectIdWithDetails(projectId)
                    .stream().map(TaskResponse::fromEntity).toList();
            taskListCache.put(projectId, tasks);
            return tasks;
        });
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
    public Task updateTask(UUID id, UpdateTaskRequest request, UUID keycloakId){
        Task task = taskRepository.getByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("This task does not exists"));

        User actingUser = authorizationService.requiredRole(task.getProject().getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.EDITOR, OrgMember.Role.MEMBER);

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());

        if (request.getAssigneeIds() != null){
            Set<UUID> currentAssigneeIds = task.getAssignees().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
            Set<UUID> incomingAssigneeIds = new HashSet<>(request.getAssigneeIds());

            //TO ADD
            Set<UUID> toAdd = new HashSet<>(incomingAssigneeIds);
            toAdd.removeAll(currentAssigneeIds);
            List<User> toAddUser = userRepository.findAllById(toAdd);

            //TO REMOVE
            Set<UUID> toRemove = new HashSet<>(currentAssigneeIds);
            toRemove.removeAll(incomingAssigneeIds);
            List<User> toRemoveUser = userRepository.findAllById(toRemove);

            if (!toAdd.isEmpty() || !toRemove.isEmpty()){
                List<User> newAssignees = userRepository.findAllById(incomingAssigneeIds);
                if(newAssignees.size() != request.getAssigneeIds().size()){
                    throw new ResourceNotFoundException("One or more assignees not found");
                }
                task.getAssignees().clear();
                task.getAssignees().addAll(newAssignees);

                //NOTIFY NEW ASSIGNEES
                for (User newAssignee : toAddUser){
                    eventPublisher.publishEvent(new TaskAssignedEvent(
                            UUID.randomUUID(),
                            task.getId(),
                            task.getTitle(),
                            task.getProject().getId(),
                            task.getProject().getOrganization().getId(),
                            newAssignee.getId(),
                            actingUser.getId(),
                            Instant.now()
                    ));
                }

                //NOTIFY REMOVED ASSIGNEE
                for (UUID removedId : toRemove) {
                    eventPublisher.publishEvent(new TaskUnassignedEvent(
                            UUID.randomUUID(),
                            task.getId(),
                            task.getTitle(),
                            task.getProject().getId(),
                            task.getProject().getOrganization().getId(),
                            actingUser.getId(),
                            removedId,
                            Instant.now()
                    ));
                }
            }
        }

        if (request.getStatus() != null && !request.getStatus().name().equals(task.getStatus().name())) {
            String oldStatus = task.getStatus().name();
            task.setStatus(request.getStatus());
//            taskActivityLogService.logStatusChanged(task, actingUser, oldStatus, request.getStatus().name());
            eventPublisher.publishEvent(new TaskStatusChangedAuditEvent(
                    UUID.randomUUID(),
                    task.getId(),
                    task.getTitle(),
                    actingUser.getId(),
                    oldStatus,
                    request.getStatus().name(),
                    Instant.now()
            ));
        }
        if (request.getPriority() != null) {
            String oldPriority = task.getPriority().name();
            task.setPriority(request.getPriority());
            taskActivityLogService.logPriorityChanged(task, actingUser, oldPriority, request.getPriority().name());
        }
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

        if (request.getLabelIds() != null){
            List<Label> labels = labelRepository.findAllById(request.getLabelIds());
            task.getLabels().clear();
            task.getLabels().addAll(labels);
        }

        eventPublisher.publishEvent(new ProjectTasksChanged(task.getProject().getId()));
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(UUID id, UUID keycloakId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        boolean isMember = projectMemberRepository.existsByProjectIdAndKeycloakId(task.getProject().getId(), keycloakId);
        if (isMember){
            taskRepository.delete(task);
            eventPublisher.publishEvent(new ProjectTasksChanged(task.getProject().getId()));
        }
        else {
            authorizationService.requiredRole(task.getProject().getOrganization().getId(), keycloakId, OrgMember.Role.OWNER, OrgMember.Role.EDITOR);
            taskRepository.delete(task);
            eventPublisher.publishEvent(new ProjectTasksChanged(task.getProject().getId()));
        }
    }

}
