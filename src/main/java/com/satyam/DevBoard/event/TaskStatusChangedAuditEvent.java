package com.satyam.DevBoard.event;

import java.time.Instant;
import java.util.UUID;

public record TaskStatusChangedAuditEvent(
        UUID eventId,
        UUID taskId,
        String title,
        UUID actorId,
        String oldStatus,
        String newStatus,
        Instant occurredAt
) implements TaskEvent {
}
