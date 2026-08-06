package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateSprintRequest {

    @NotNull(message = "Project cannot be null")
    private UUID projectId;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String goal;

    @NotNull(message = "Start date cannot be blank")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be blank")
    private LocalDate endDate;
}
