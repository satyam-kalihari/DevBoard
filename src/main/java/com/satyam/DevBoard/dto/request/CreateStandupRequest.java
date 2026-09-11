package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateStandupRequest {

    @NotNull(message= "Project ID cannot be null ")
    private UUID projectId;

    @Pattern(regexp = "^(MON|TUE|WED|THU|FRI|SAT|SUN)(,(MON|TUE|WED|THU|FRI|SAT|SUN))*$",
            message = "Days must be comma-separated abbreviations, e.g., MON,TUE,WED")
    private String scheduleDays;

    @NotNull(message = "Time cannot be blank")
    private LocalTime scheduledTime;

    private List<UUID> runsId;
}
