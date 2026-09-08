package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.ProjectMember;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProjectMemberResponse {
    private UUID id;
    private String projectName;
    private UUID projectId;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userAvatarUrl;
    private LocalDateTime joinedAt;

    public static ProjectMemberResponse fromEntity(ProjectMember projectMember){

        return ProjectMemberResponse.builder()
                .id(projectMember.getId())
                .projectId(projectMember.getProject().getId())
                .projectName(projectMember.getProject().getName())
                .userId(projectMember.getUser().getId())
                .userName(projectMember.getUser().getName())
                .userEmail(projectMember.getUser().getEmail())
                .userAvatarUrl(projectMember.getUser().getAvatarUrl())
                .joinedAt(projectMember.getJoinedAt())
                .build();
    }
}
