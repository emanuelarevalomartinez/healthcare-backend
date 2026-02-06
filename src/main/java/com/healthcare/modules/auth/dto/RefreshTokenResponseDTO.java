package com.healthcare.modules.auth.dto;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}
