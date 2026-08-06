package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class CreateNotificationRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Organization ID is required")
    private UUID orgId;

    @NotBlank(message = "Notification type cannot be blank")
    private Notification.Type type;

    @NotBlank(message = "Notification title cannot be blank")
    private String title;

    private Map<String, Object> payload;
}
