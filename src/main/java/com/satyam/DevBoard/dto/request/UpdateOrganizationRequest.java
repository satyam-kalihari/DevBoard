package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateOrganizationRequest {

    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Pattern(
            regexp = "^[a-z0-9-]{2,100}$",
            message = "Slug must be lowercase letters, numbers, and hyphens only"
    )
    private String slug;

    @Pattern(
            regexp = "^[A-Z0-9]{2,10}$",
            message = "Code must be 2-10 uppercase letters and numbers only"
    )
    private String code;

    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;

    @Size(max = 100, message = "Timezone cannot exceed 100 characters")
    private String timeZone;

    private String avatarUrl;
}