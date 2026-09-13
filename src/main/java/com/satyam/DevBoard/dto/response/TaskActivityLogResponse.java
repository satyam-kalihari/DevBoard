package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.TaskActivityLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class TaskActivityLogResponse {

    private UUID id;
    private UUID taskId;
    private String taskTitle;
    private UUID actorId;
    private String actorName;
    private String actionType;
    private Map<String, Object> beforeState;
    private Map<String, Object> afterState;
    private LocalDateTime occurredAt;

    public static TaskActivityLogResponse fromEntity(TaskActivityLog log) {
        return TaskActivityLogResponse.builder()
                .id(log.getId())
                .taskId(log.getTask().getId())
                .taskTitle(log.getTask().getTitle())
                .actorId(log.getActor().getId())
                .actorName(log.getActor().getName())
                .actionType(log.getActionType())
                .beforeState(log.getBeforeState())
                .afterState(log.getAfterState())
                .occurredAt(log.getOccurredAt())
                .build();
    }
}