package com.satyam.DevBoard.event;

import java.time.Instant;
import java.util.UUID;

public record TaskAssignedEvent(
        UUID eventId,
        UUID taskId,
        String taskTitle,
        UUID projectId,
        UUID orgId,
        UUID assigneeId,
        Instant occurredAt
) { }
