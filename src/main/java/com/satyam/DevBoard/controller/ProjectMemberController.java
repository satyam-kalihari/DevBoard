package com.satyam.DevBoard.controller;

import com.satyam.DevBoard.dto.request.CreateProjectMemberRequest;
import com.satyam.DevBoard.dto.response.ProjectMemberResponse;
import com.satyam.DevBoard.model.ProjectMember;
import com.satyam.DevBoard.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/project_members")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> createProjectMember(
            @Valid @RequestBody CreateProjectMemberRequest request
            )
    {
        ProjectMember member = projectMemberService.createProjectMember(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectMemberResponse.fromEntity(member));
    }

    @GetMapping(params = "projectId")
    public ResponseEntity<List<ProjectMemberResponse>> getMembersOfProject(@RequestParam UUID projectId){
        List<ProjectMemberResponse> members = projectMemberService.getAllMemberByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(members);
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<ProjectMemberResponse>> getProjectsOfMember(@RequestParam UUID userId){
        List<ProjectMemberResponse> members = projectMemberService.getAllProjectByUserId(userId)
                .stream()
                .map(ProjectMemberResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectMemberResponse> getProjectMemberById(@PathVariable UUID id){
        ProjectMember member = projectMemberService.getProjectMemberById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ProjectMemberResponse.fromEntity(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable UUID id){
        projectMemberService.deleteProjectMember(id);

        return ResponseEntity.noContent().build();
    }
}
