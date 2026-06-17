package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;

import java.util.UUID;

public record DoctorResponseWithUserDTO(
        UUID userId,
        String username,
        String email,
        UserRole role,
        Boolean isActive,
        UUID doctorId,
        String specialty,
        String licenseNumber,
        Integer defaultConsultationDuration
) {

    public static DoctorResponseWithUserDTO fromEntity(UserEntity user, DoctorEntity doctor) {

        return new DoctorResponseWithUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                doctor.getId(),
                doctor.getSpecialty(),
                doctor.getLicenseNumber(),
                doctor.getDefaultConsultationDuration()
        );
    }

}
