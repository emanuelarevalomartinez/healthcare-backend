package com.healthcare.modules.auth.dto;

import com.healthcare.modules.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterResponseDTO(
        String accessToken,
        String refreshToken
) {
}
