package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTaskCommentRequest {

    @NotBlank(message = "Comment body cannot be blank")
    private String body;
}