package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor.entity.DoctorEntity;

import java.util.UUID;

public record DoctorResponseDTO(
        UUID id,
        UUID createdBy,
        String specialty,
        String licenseNumber,
        Integer defaultConsultationDuration

) {
    public static DoctorResponseDTO fromEntity(DoctorEntity doctor) {

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getCreatedBy().getId(),
                doctor.getSpecialty(),
                doctor.getLicenseNumber(),
                doctor.getDefaultConsultationDuration()
        );
    }
}
