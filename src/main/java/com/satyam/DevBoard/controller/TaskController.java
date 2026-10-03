package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateTaskRequest;
import com.satyam.DevBoard.dto.request.UpdateTaskRequest;
import com.satyam.DevBoard.dto.response.TaskResponse;
import com.satyam.DevBoard.exception.ResourceNotFoundException;
import com.satyam.DevBoard.model.Task;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.UserRepository;
import com.satyam.DevBoard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal Jwt jwt
            ){

        UUID keycloakUserId = UUID.fromString(jwt.getSubject());
        User actingUser = userRepository.findByKeycloakId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not provisioned"));

        Task task = taskService.createTask(request, keycloakUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.fromEntity(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getByIdWithDetails(@PathVariable UUID id){
        Task task = taskService.getByIdWithDetails(id);
        return ResponseEntity.status(HttpStatus.OK).body(TaskResponse.fromEntity(task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        UUID keycloakId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        Task task = taskService.updateTask(id, request, keycloakId);
        return ResponseEntity.ok(TaskResponse.fromEntity(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {

        UUID keycloakId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        taskService.deleteTask(id, keycloakId);
        return ResponseEntity.noContent().build();
    }
}
