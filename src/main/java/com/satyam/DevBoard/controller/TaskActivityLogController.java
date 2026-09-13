package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.response.TaskActivityLogResponse;
import com.satyam.DevBoard.service.TaskActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskActivityLogController {

    private final TaskActivityLogService taskActivityLogService;

    @GetMapping("/{taskId}/activity")
    public ResponseEntity<List<TaskActivityLogResponse>> getActivityByTask(
            @PathVariable UUID taskId
    ) {
        List<TaskActivityLogResponse> logs = taskActivityLogService.getAllByTaskId(taskId)
                .stream()
                .map(TaskActivityLogResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/activity")
    public ResponseEntity<List<TaskActivityLogResponse>> getActivityByActor(
            @RequestParam UUID actorId
    ) {
        List<TaskActivityLogResponse> logs = taskActivityLogService.getAllByActorId(actorId)
                .stream()
                .map(TaskActivityLogResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(logs);
    }
}