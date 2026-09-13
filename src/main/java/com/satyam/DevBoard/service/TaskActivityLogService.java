package com.satyam.DevBoard.service;

import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Task;
import com.satyam.DevBoard.model.TaskActivityLog;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.TaskActivityLogRepository;
import com.satyam.DevBoard.repository.TaskRepository;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskActivityLogService {

    private final TaskActivityLogRepository taskActivityLogRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // ── Core log method ───────────────────────────────────────────────────────
    // Called internally by TaskService whenever something changes on a task.
    // Never exposed directly through an HTTP endpoint.

    @Transactional
    public TaskActivityLog log(
            Task task,
            User actor,
            String actionType,
            Map<String, Object> beforeState,
            Map<String, Object> afterState
    ) {
        TaskActivityLog entry = new TaskActivityLog();
        entry.setTask(task);
        entry.setActor(actor);
        entry.setActionType(actionType);
        entry.setBeforeState(beforeState);
        entry.setAfterState(afterState);

        return taskActivityLogRepository.save(entry);
    }

    // ── Convenience methods for each action type ──────────────────────────────
    // TaskService calls these — no raw strings, no map construction outside this class

    @Transactional
    public void logTaskCreated(Task task, User actor) {
        log(task, actor, TaskActivityLog.ACTION_TASK_CREATED,
                null,
                Map.of("title", task.getTitle(),
                        "status", task.getStatus().name(),
                        "priority", task.getPriority().name()));
    }

    @Transactional
    public void logStatusChanged(Task task, User actor, String oldStatus, String newStatus) {
        log(task, actor, TaskActivityLog.ACTION_STATUS_CHANGED,
                Map.of("status", oldStatus),
                Map.of("status", newStatus));
    }

    @Transactional
    public void logPriorityChanged(Task task, User actor, String oldPriority, String newPriority) {
        log(task, actor, TaskActivityLog.ACTION_PRIORITY_CHANGED,
                Map.of("priority", oldPriority),
                Map.of("priority", newPriority));
    }

    @Transactional
    public void logAssigneeAdded(Task task, User actor, User assignee) {
        log(task, actor, TaskActivityLog.ACTION_ASSIGNEE_ADDED,
                null,
                Map.of("userId", assignee.getId().toString(),
                        "userName", assignee.getName()));
    }

    @Transactional
    public void logAssigneeRemoved(Task task, User actor, User assignee) {
        log(task, actor, TaskActivityLog.ACTION_ASSIGNEE_REMOVED,
                Map.of("userId", assignee.getId().toString(),
                        "userName", assignee.getName()),
                null);
    }

    @Transactional
    public void logSprintChanged(Task task, User actor, String oldSprintName, String newSprintName) {
        log(task, actor, TaskActivityLog.ACTION_SPRINT_CHANGED,
                Map.of("sprint", oldSprintName != null ? oldSprintName : "Backlog"),
                Map.of("sprint", newSprintName != null ? newSprintName : "Backlog"));
    }

    @Transactional
    public void logCommentAdded(Task task, User actor) {
        log(task, actor, TaskActivityLog.ACTION_COMMENT_ADDED,
                null,
                Map.of("actorName", actor.getName()));
    }

    @Transactional
    public void logDueDateChanged(Task task, User actor, String oldDate, String newDate) {
        log(task, actor, TaskActivityLog.ACTION_DUE_DATE_CHANGED,
                Map.of("dueDate", oldDate != null ? oldDate : "none"),
                Map.of("dueDate", newDate != null ? newDate : "none"));
    }

    // ── Read operations ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TaskActivityLog> getAllByTaskId(UUID taskId) {
        return taskActivityLogRepository.findAllByTaskIdWithDetails(taskId);
    }

    @Transactional(readOnly = true)
    public List<TaskActivityLog> getAllByActorId(UUID actorId) {
        return taskActivityLogRepository.findAllByActorIdWithDetails(actorId);
    }
}