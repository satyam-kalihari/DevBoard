package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.Task;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateTaskRequest {

    @Size(min = 1, max = 255, message = "Title cannot be empty if provided")
    private String title;

    private String description;

    private Task.Status status;

    private Task.Priority priority;

    private Integer storyPoints;

    private LocalDate dueDate;

    private UUID sprintId;

    private UUID parentTaskId;

    private List<UUID> assigneeIds;

    private List<UUID> labelIds;
}