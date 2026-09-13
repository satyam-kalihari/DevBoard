package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.TaskComment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TaskCommentResponse {

    private UUID id;
    private UUID taskId;
    private UUID userId;
    private String userName;
    private String userAvatarUrl;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskCommentResponse fromEntity(TaskComment comment) {
        return TaskCommentResponse.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .userAvatarUrl(comment.getUser().getAvatarUrl())
                .body(comment.getBody())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}