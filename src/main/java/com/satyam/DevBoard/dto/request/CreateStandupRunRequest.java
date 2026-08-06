package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateStandupRunRequest {

    @NotNull(message = "Standup ID is required")
    private UUID standupId;

    @NotNull(message = "Run date is required")
    private LocalDate runDate;

}