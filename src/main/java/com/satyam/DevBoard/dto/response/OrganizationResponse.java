package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.Organization;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrganizationResponse {

    private UUID id;
    private String name;
    private String slug;
    private String code;
    private String avatarUrl;
    private String location;
    private String timeZone;
    private boolean isActive;
    private LocalDateTime createdAt;

    public static OrganizationResponse fromEntity(Organization org){
        return OrganizationResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .slug(org.getSlug())
                .code(org.getCode())
                .avatarUrl(org.getAvatarUrl())
                .location(org.getLocation())
                .timeZone(org.getTimeZone())
                .isActive(org.isActive())
                .createdAt(org.getCreatedAt())
                .build();
    }
}
