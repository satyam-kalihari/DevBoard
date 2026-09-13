package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateTaskCommentRequest;
import com.satyam.DevBoard.dto.request.UpdateTaskCommentRequest;
import com.satyam.DevBoard.dto.response.TaskCommentResponse;
import com.satyam.DevBoard.model.TaskComment;
import com.satyam.DevBoard.service.TaskCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<TaskCommentResponse> createComment(
            @PathVariable UUID taskId,
            @Valid @RequestBody CreateTaskCommentRequest request
    ) {
        request.setTaskId(taskId);
        TaskComment comment = taskCommentService.createTaskComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskCommentResponse.fromEntity(comment));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<TaskCommentResponse>> getAllCommentsByTask(
            @PathVariable UUID taskId
    ) {
        List<TaskCommentResponse> comments = taskCommentService.getAllByTaskId(taskId)
                .stream()
                .map(TaskCommentResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<TaskCommentResponse> updateComment(
            @PathVariable UUID commentId,
            @Valid @RequestBody UpdateTaskCommentRequest request
    ) {
        TaskComment comment = taskCommentService.updateTaskComment(commentId, request);
        return ResponseEntity.ok(TaskCommentResponse.fromEntity(comment));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        taskCommentService.deleteTaskComment(commentId);
        return ResponseEntity.noContent().build();
    }
}