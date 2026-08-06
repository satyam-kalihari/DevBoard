package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateStandupRequest {

    @NotNull(message= "Project ID cannot be null ")
    private UUID projectId;

    @NotBlank(message = "Schedule day cannot be blank")
    private String scheduleDays = "MON,TUE,WED,THU,FRI";

    @NotNull(message = "Time cannot be blank")
    private LocalTime scheduledTime;

    private List<UUID> runsId;
}
