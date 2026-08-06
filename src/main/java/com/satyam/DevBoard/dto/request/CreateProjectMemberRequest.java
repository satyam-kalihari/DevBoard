package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateProjectMemberRequest {
    @NotNull(message = "Please provide project ID")
    private UUID projectId;

    @NotNull(message = "Please provide user ID")
    private UUID userId;
}
