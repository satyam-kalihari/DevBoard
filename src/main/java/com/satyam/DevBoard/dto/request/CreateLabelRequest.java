package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.Size;


@Data
public class CreateLabelRequest {

    @NotBlank(message = "Label name is required")
    @Size(min = 1, max = 50, message = "Label name should not exceed 50 characters")
    private String name;

    @Pattern(regexp = "^#[A-Za-z0-9]{6}$", message = "Color must be a valid hex code, e.g. #6366F1")
    private String colorHex;
}
