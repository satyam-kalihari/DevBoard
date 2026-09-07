package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.Project;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private Project.Status status;
    private LocalDate startDate;
    private LocalDate targetDate;

    public static ProjectResponse fromEntity(Project project){
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .targetDate(project.getTargetDate())
                .build();
    }
}
