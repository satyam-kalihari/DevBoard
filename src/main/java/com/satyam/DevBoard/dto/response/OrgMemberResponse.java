package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.OrgMember;
import com.satyam.DevBoard.model.Organization;
import com.satyam.DevBoard.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrgMemberResponse {

    private UUID id;
    private UUID orgId;
    private String orgName;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userAvatarUrl;
    private OrgMember.Role role;
    private LocalDateTime joinedAt;

    public static OrgMemberResponse fromEntity(OrgMember member){
        return OrgMemberResponse.builder()
                .id(member.getId())
                .orgId(member.getOrganization().getId())
                .orgName(member.getOrganization().getName())
                .userId(member.getUser().getId())
                .userName(member.getUser().getName())
                .userEmail(member.getUser().getEmail())
                .userAvatarUrl(member.getUser().getAvatarUrl())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
