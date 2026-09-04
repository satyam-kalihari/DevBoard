package com.satyam.DevBoard.dto.response;

import com.satyam.DevBoard.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String avatarUrl;
    private boolean isActive;
    private LocalDateTime createdAt;

    public static UserResponse fromEntity(User user){
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
