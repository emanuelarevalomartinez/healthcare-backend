package com.healthcare.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequestDTO(

        @NotBlank(message = "El refresh token es obligatorio")
        @Size(max = 512, message = "El refresh token no puede exceder 512 caracteres")
        String refreshToken
) {
}
