package com.satyam.DevBoard.event;

import java.time.Instant;
import java.util.UUID;

public record TaskStatusChangedEvent(
        UUID eventId,
        UUID taskId,
        String taskTitle,
        UUID projectId,
        UUID orgId,
        UUID actorId,
        String status,
        Instant occurredAt
) implements TaskEvent {
}
