package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateNotificationRequest;
import com.satyam.DevBoard.dto.request.UpdateNotificationRequest;
import com.satyam.DevBoard.dto.response.NotificationResponse;
import com.satyam.DevBoard.model.Notification;
import com.satyam.DevBoard.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Internal use — called by other services, not typically exposed to end users
    // Once Keycloak is set up, this endpoint will be secured or removed
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request
    ) {
        Notification notification = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NotificationResponse.fromEntity(notification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable UUID id) {
        Notification notification = notificationService.getByIdWithDetails(id);
        return ResponseEntity.ok(NotificationResponse.fromEntity(notification));
    }

    // GET /api/v1/notifications?userId=xxx  — all notifications for a user
    @GetMapping(params = "userId")
    public ResponseEntity<List<NotificationResponse>> getAllByUserId(
            @RequestParam UUID userId
    ) {
        List<NotificationResponse> notifications = notificationService.getAllByUserId(userId)
                .stream()
                .map(NotificationResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(notifications);
    }

    // GET /api/v1/notifications/unread?userId=xxx
    @GetMapping(value = "/unread", params = "userId")
    public ResponseEntity<List<NotificationResponse>> getUnreadByUserId(
            @RequestParam UUID userId
    ) {
        List<NotificationResponse> notifications = notificationService.getUnreadByUserId(userId)
                .stream()
                .map(NotificationResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(notifications);
    }

    // GET /api/v1/notifications/unread/count?userId=xxx
    // Returns just the count — used to show the bell badge number in the UI
    @GetMapping(value = "/unread/count", params = "userId")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam UUID userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    // PATCH /api/v1/notifications/{id} — mark single notification as read/unread
    @PatchMapping("/{id}")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNotificationRequest request
    ) {
        Notification notification = notificationService.markAsRead(id, request);
        return ResponseEntity.ok(NotificationResponse.fromEntity(notification));
    }

    // PATCH /api/v1/notifications/mark-all-read?userId=xxx
    // Bulk operation — no response body needed, just 204
    @PatchMapping(value = "/mark-all-read", params = "userId")
    public ResponseEntity<Void> markAllAsRead(@RequestParam UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}