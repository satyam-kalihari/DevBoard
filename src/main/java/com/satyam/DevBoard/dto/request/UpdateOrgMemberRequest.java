package com.satyam.DevBoard.dto.request;

import com.satyam.DevBoard.model.OrgMember;
import lombok.Data;

@Data
public class UpdateOrgMemberRequest {
    private OrgMember.Role role;
}
