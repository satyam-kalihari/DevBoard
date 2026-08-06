package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Name is required.")
    @Size(max = 50,message = "Name cannot be more than 50 character")
    private String name;

    private String description;

    @NotNull(message = "Status is required")
    private Project.Status status;

    private LocalDate startDate;
    private LocalDate targetDate;
    private LocalDate endDate;
}
