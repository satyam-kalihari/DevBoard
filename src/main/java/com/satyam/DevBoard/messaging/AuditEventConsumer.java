package com.satyam.DevBoard.messaging;

import com.satyam.DevBoard.config.KafkaTopicConfig;
import com.satyam.DevBoard.event.TaskAssignedEvent;
import com.satyam.DevBoard.event.TaskCreatedEvent;
import com.satyam.DevBoard.model.TaskActivityLog;
import com.satyam.DevBoard.service.TaskActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = KafkaTopicConfig.TASK_EVENTS, groupId = "devboard-audit")
public class AuditEventConsumer {

    private final TaskActivityLogService activityLogService;

    @KafkaHandler
    public void onTaskCreated(TaskCreatedEvent e) {
        activityLogService.recordFromEvent(e.eventId(), e.taskId(), e.actorId(),
                TaskActivityLog.ACTION_TASK_CREATED, null,
                Map.of("title", e.taskTitle(), "status", e.status(), "priority", e.priority()));
    }

    @KafkaHandler
    public void onTaskAssigned(TaskAssignedEvent e) {
        activityLogService.recordFromEvent(e.eventId(), e.taskId(), e.actorId(),
                TaskActivityLog.ACTION_ASSIGNEE_ADDED, null,
                Map.of("userId", e.assigneeId().toString()));
    }

    @KafkaHandler(isDefault = true)
    public void ignoreOthers(Object event) {}
}