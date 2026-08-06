package com.satyam.DevBoard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrganizationRequest {

    @NotBlank(message = "Organization name is required")
    @Size(max = 50, message = "Organization name cannot exceed 50 character")
    private String name;

    @NotBlank(message = "Slug is required")
    private String slug;

    @NotBlank(message = "Location is required")
    private String location;

    private String avatarUrl;

    @NotBlank(message = "Timezone is required")
    private String timeZone;
}
