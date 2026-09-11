package com.satyam.DevBoard.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateStandupRequest {

    private String scheduleDays;

    private Boolean isActive;
    private List<UUID> runIds;

    private LocalTime scheduledTime;
}
