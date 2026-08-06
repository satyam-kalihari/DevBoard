package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateTaskRequest {

    @NotNull(message = "Project ID is required")
    private UUID projectId;

    private UUID sprintId;

    private UUID parentTaskId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    private Task.Priority priority;

    private Integer storyPoints;

    private LocalDate dueDate;

    private List<UUID> assigneesIds;

    private List<UUID> labelIds;
}
