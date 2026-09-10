package com.healthcare.modules.user.dto;

import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor_schedule.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserWithDoctorAndSchedulesResponseDTO(
        UUID id,
        String username,
        String email,
        UserRole role,
        Boolean isActive,
        DoctorResponseDTO doctor,
        List<DoctorScheduleResponseDTO> schedules,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin
) {

    public static UserWithDoctorAndSchedulesResponseDTO fromEntity(UserEntity user) {
        DoctorEntity doctor = user.getDoctor();

        return new UserWithDoctorAndSchedulesResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                doctor != null ? DoctorResponseDTO.fromEntity(doctor) : null,
                doctor != null ? doctor.getSchedules().stream()
                        .map(DoctorScheduleResponseDTO::fromEntity)
                        .collect(Collectors.toList()) : List.of(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin()
        );
    }

}
