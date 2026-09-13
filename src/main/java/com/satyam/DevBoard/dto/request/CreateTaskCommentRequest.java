package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateTaskCommentRequest {

    @NotNull(message = "Task ID is required")
    private UUID taskId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Comment body cannot be blank")
    private String body;
}