package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.Project;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateProjectRequest {
    private String name;
    private String description;
    private Project.Status status;
    private LocalDate startDate;
    private LocalDate targetDate;
    private LocalDate endDate;
}
