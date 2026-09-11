package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateSprintRequest;
import com.satyam.DevBoard.dto.request.UpdateSprintRequest;
import com.satyam.DevBoard.dto.response.SprintResponse;
import com.satyam.DevBoard.dto.response.TaskResponse;
import com.satyam.DevBoard.model.Sprint;
import com.satyam.DevBoard.service.SprintService;
import com.satyam.DevBoard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sprints")
@RequiredArgsConstructor
public class SprintController {
    private final SprintService sprintService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(
            @Valid @RequestBody CreateSprintRequest request
            ){
        Sprint sprint = sprintService.createSprint(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SprintResponse.fromEntity(sprint));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintResponse> getSprintById(@PathVariable UUID id){
        Sprint sprint = sprintService.getSprintWithDetails(id);
        return ResponseEntity.ok(SprintResponse.fromEntity(sprint));
    }

    @GetMapping("/{sprintId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksBySprintId(@PathVariable UUID sprintId) {
        List<TaskResponse> tasks = taskService.getAllBySprintIdWithDetails(sprintId)
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(tasks);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<SprintResponse> updateSprint(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSprintRequest request
            ){
        Sprint sprint = sprintService.updateSprint(id, request);

        return ResponseEntity.ok(SprintResponse.fromEntity(sprint));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable UUID id){
        sprintService.deleteSprint(id);
        return ResponseEntity.noContent().build();
    }
}
