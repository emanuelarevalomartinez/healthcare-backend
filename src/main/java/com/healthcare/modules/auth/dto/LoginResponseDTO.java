package com.healthcare.modules.auth.dto;

import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResponseDTO(
        UUID id,
        String username,
        String email,
        UserRole role,
        Boolean active,
        DoctorResponseDTO doctor,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin,
        String accessToken,
        String refreshToken
) {

    public static LoginResponseDTO fromEntity(UserEntity user, String accessToken, String refreshToken) {

        return new LoginResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getDoctor() != null
                        ? DoctorResponseDTO.fromEntity(user.getDoctor())
                        : null,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin(),
                accessToken,
                refreshToken
        );
    }

}
