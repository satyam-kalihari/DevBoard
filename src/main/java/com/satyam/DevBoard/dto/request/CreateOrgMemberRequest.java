package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.OrgMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrgMemberRequest {

    @NotNull(message = "Organization ID is required")
    private UUID orgId;

    @NotNull(message = "User Id is missing")
    private UUID userId;

    @NotBlank(message = "Role is blank")
    private OrgMember.Role role;
}
