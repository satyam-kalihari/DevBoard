package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateProjectRequest;
import com.satyam.DevBoard.dto.request.UpdateProjectRequest;
import com.satyam.DevBoard.dto.response.ProjectResponse;
import com.satyam.DevBoard.model.Project;
import com.satyam.DevBoard.service.ProjectService;
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

    @PatchMapping
    public ResponseEntity<ProjectResponse> updateProject(
            @Valid @RequestBody UpdateProjectRequest request
            ){

        Project project = projectService.updateProject(request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ProjectResponse.fromEntity(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id){
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
