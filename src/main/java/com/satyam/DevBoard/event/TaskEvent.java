package com.satyam.DevBoard.event;
import java.util.UUID;

public interface TaskEvent {
    UUID eventId();
    UUID taskId();
}