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
    private UUID orgId;
    private String orgName;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userAvatarUrl;
    private String description;
    private Project.Status status;
    private LocalDate startDate;
    private LocalDate targetDate;

    public static ProjectResponse fromEntity(Project project){
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .orgId(project.getOrganization().getId())
                .orgName(project.getOrganization().getName())
                .userId(project.getLeadUser().getId())
                .userName(project.getLeadUser().getName())
                .userEmail(project.getLeadUser().getEmail())
                .userAvatarUrl(project.getLeadUser().getAvatarUrl())
                .description(project.getDescription())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .targetDate(project.getTargetDate())
                .build();
    }
}
