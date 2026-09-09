package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Sprint;
import com.satyam.DevBoard.model.Task;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SprintResponse {
    private UUID id;
    private UUID projectId;
    private String projectName;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private Sprint.Status status;
    private List<Task> tasks;
    private LocalDateTime createdAt;

    public static SprintResponse fromEntity(Sprint sprint){
        return SprintResponse.builder()
                .id(sprint.getId())
                .projectId(sprint.getProject().getId())
                .projectName(sprint.getProject().getName())
                .name(sprint.getName())
                .goal(sprint.getGoal())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .status(sprint.getStatus())
                .tasks(sprint.getTasks())
                .createdAt(sprint.getCreatedAt())
                .build();
    }
}
