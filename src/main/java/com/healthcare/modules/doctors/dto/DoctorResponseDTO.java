package com.healthcare.modules.doctors.dto;

import com.healthcare.modules.doctors.entity.DoctorEntity;

import java.util.UUID;

public record DoctorResponseDTO(
        UUID id,
        UUID modifiedBy,
        String specialty,
        String licenseNumber,
        Integer defaultConsultationDuration

) {
    public static DoctorResponseDTO fromEntity(DoctorEntity doctor) {

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getModifiedBy().getId(),
                doctor.getSpecialty(),
                doctor.getLicenseNumber(),
                doctor.getDefaultConsultationDuration()
        );
    }
}
