package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNotificationRequest {

    @NotNull(message = "isRead is required")
    private Boolean isRead;
}