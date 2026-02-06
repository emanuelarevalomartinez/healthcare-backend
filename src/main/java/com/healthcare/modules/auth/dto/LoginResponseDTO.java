package com.healthcare.modules.auth.dto;

import com.healthcare.modules.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResponseDTO(
        UUID id,
        String username,
        String email,
        UserRole role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin,
        String accessToken,
        String refreshToken
) {
}
