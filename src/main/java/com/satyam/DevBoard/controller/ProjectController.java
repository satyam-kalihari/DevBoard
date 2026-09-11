package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateProjectRequest;
import com.satyam.DevBoard.dto.request.UpdateProjectRequest;
import com.satyam.DevBoard.dto.response.ProjectResponse;
import com.satyam.DevBoard.dto.response.SprintResponse;
import com.satyam.DevBoard.dto.response.TaskResponse;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.service.ProjectService;
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
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final SprintService sprintService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ){
        Project project = projectService.createProject(request.getOrgId(), request.getUserId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.fromEntity(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id){
        Project project = projectService.getProjectById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ProjectResponse.fromEntity(project));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProject(){
        List<ProjectResponse> projects = projectService.getAllProject()
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(@PathVariable UUID projectId) {
        List<TaskResponse> tasks = taskService.getAllByProjectIdWithDetails(projectId)
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{projectId}/tasks/backlog")
    public ResponseEntity<List<TaskResponse>> getBacklogTasksByProjectId(@PathVariable UUID projectId) {
        List<TaskResponse> tasks = taskService.getBacklogTaskByProjectId(projectId)
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{projectId}/sprints")
    public ResponseEntity<List<SprintResponse>> getAllByProjectId(@PathVariable UUID projectId){
        List<SprintResponse> sprints = sprintService.getAllByProjectIdWithDetails(projectId)
                .stream()
                .map(SprintResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(sprints);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequest request
            ){

        Project project = projectService.updateProject(id, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ProjectResponse.fromEntity(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id){
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
