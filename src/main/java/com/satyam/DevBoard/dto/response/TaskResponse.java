package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.Task;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TaskResponse {

    private UUID id;
    private UUID projectId;
    private UUID sprintId;
    private UUID parentTaskId;

    private String title;
    private String description;

    private Task.Status status;
    private Task.Priority priority;

    private Integer storyPoints;
    private Integer rank;

    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<UserResponse> assignees;
    private List<LabelResponse> labels;

    public static TaskResponse fromEntity(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .projectId(task.getProject().getId())

//                CHECKING IF THE TASK IS IN A SPRINT OR HAVE PARENT TASK
                .sprintId(task.getSprint() != null ? task.getSprint().getId() : null)
                .parentTaskId(task.getParentTask() != null ? task.getParentTask().getId() : null)
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .storyPoints(task.getStoryPoints())
                .rank(task.getRank())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())

//                TAKING USER OBJECTS AS AN INPUT -> CONVERTING THEM TO USER_RESPONSE -> SENDING LIST OF USER_RESPONSE
                .assignees(task.getAssignees() == null ? List.of() : task.getAssignees()
                        .stream()
                        .map(UserResponse::fromEntity)
                        .toList())

//                TAKING LABEL OBJECT -> CONVERTING THEM INTO LABEL_RESPONSE -> SENDING LIST OF LABEL_RESPONSE
                .labels(task.getLabels() == null ? List.of() :
                        task.getLabels()
                                .stream()
                                .map(LabelResponse::fromEntity)
                                .toList())
                .build();
    }
}
