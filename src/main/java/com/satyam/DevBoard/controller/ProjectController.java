package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateProjectRequest;
import com.satyam.DevBoard.dto.request.UpdateProjectRequest;
import com.satyam.DevBoard.dto.response.ProjectResponse;
import com.satyam.DevBoard.dto.response.SprintResponse;
import com.satyam.DevBoard.dto.response.StandupResponse;
import com.satyam.DevBoard.dto.response.TaskResponse;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.model.Standup;
import com.satyam.DevBoard.service.ProjectService;
import com.satyam.DevBoard.service.SprintService;
import com.satyam.DevBoard.service.StandupService;
import com.satyam.DevBoard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private final StandupService standupService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt
            ){

        UUID keycloak = UUID.fromString(jwt.getSubject());
        Project project = projectService.createProject(request.getOrgId(), request.getUserId(), request, keycloak);

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
    public ResponseEntity<List<SprintResponse>> getAllByProjectId(@PathVariable UUID projectId, @AuthenticationPrincipal Jwt jwt){

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        List<SprintResponse> sprints = sprintService.getAllByProjectIdWithDetails(projectId, keycloakId)
                .stream()
                .map(SprintResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{projectId}/standups")
    public ResponseEntity<StandupResponse> getStandupByProjectIdWithDetails(@PathVariable UUID projectId){

        Standup standup = standupService.getStandupByProjectIdWithDetails(projectId);
        return ResponseEntity.ok(StandupResponse.fromEntity(standup));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt
            ){

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        Project project = projectService.updateProject(id, request, keycloakId);

        return ResponseEntity.status(HttpStatus.OK).body(ProjectResponse.fromEntity(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt){

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        projectService.deleteProject(id, keycloakId);
        return ResponseEntity.noContent().build();
    }
}
