package com.healthcare.modules.user.dto;

import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        UserRole role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin
) {

    public static UserResponseDTO fromEntity(UserEntity user) {

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin()
        );
    }

}
