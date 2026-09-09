package com.healthcare.modules.user.dto;

import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserWithDoctorResponseDTO(
        UUID id,
        String username,
        String email,
        UserRole role,
        Boolean isActive,
        DoctorResponseDTO doctor,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin
) {

    public static UserWithDoctorResponseDTO fromEntity(UserEntity user) {

        return new UserWithDoctorResponseDTO(
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
                user.getLastLogin()
        );
    }

}
