package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateTaskRequest;
import com.satyam.DevBoard.dto.request.UpdateTaskRequest;
import com.satyam.DevBoard.dto.response.TaskResponse;
import com.satyam.DevBoard.model.Task;
import com.satyam.DevBoard.service.TaskService;
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
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request
            ){
        Task task = taskService.createTask(request);

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
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        Task task = taskService.updateTask(id, request);
        return ResponseEntity.ok(TaskResponse.fromEntity(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
