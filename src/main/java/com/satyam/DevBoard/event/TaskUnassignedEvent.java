package com.satyam.DevBoard.event;

import java.time.Instant;
import java.util.UUID;

public record TaskUnassignedEvent(
        UUID eventId,
        UUID taskId,
        String taskTitle,
        UUID projectId,
        UUID orgId,
        UUID actorId,
        UUID assigneeId,
        Instant occurredAt
) implements TaskEvent {
}