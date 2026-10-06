package com.satyam.DevBoard.messaging;

import com.satyam.DevBoard.config.KafkaTopicConfig;
import com.satyam.DevBoard.event.TaskAssignedEvent;
import com.satyam.DevBoard.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = KafkaTopicConfig.TASK_EVENTS, groupId = "devboard-notifications")
    public void onTaskAssigned(TaskAssignedEvent event){
        log.info("Received {} for assignee {}", event.eventId(), event.assigneeId());
        notificationService.notifyTaskAssigned(event);
    }
}
