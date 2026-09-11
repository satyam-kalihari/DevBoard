package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.model.StandupRun;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class StandupResponse {

    private UUID id;
    private UUID projectId;

    private String projectName;
    private String scheduleDays;

    private boolean isActive;
//    private List<StandupRun> runs;

    private LocalTime scheduledTime;
    private LocalDateTime createdAt;

    public static StandupResponse fromEntity(Standup standup){
        return StandupResponse.builder()
                .id(standup.getId())
                .projectId(standup.getProject().getId())
                .projectName(standup.getProject().getName())
                .scheduleDays(standup.getScheduleDays())
                .isActive(standup.getIsActive())
//                .runs(standup.getRuns() == null ? List.of() : standup.getRuns()
//                        .stream().toList())
                .scheduledTime(standup.getScheduledTime())
                .createdAt(standup.getCreatedAt())
                .build();
    }
}
