package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateStandupResponseRequest {

    @NotNull(message = "Standup ID is required")
    private UUID standupRunId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Please provide what you worked on yesterday")
    private String answerYesterday;

    @NotBlank(message = "Please provide what you will work on today")
    private String answerToday;

    private String answerBlockers;

    private boolean hasBlockers = false;
}
