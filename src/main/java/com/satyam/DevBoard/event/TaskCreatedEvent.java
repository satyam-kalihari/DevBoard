package com.satyam.DevBoard.event;

import java.time.Instant;
import java.util.UUID;

public record TaskCreatedEvent(
        UUID eventId,
        UUID taskId,
        String taskTitle,
        UUID projectId,
        UUID orgId,
        UUID actorId,
        String status,
        String priority,
        Instant occurredAt
) implements TaskEvent {}