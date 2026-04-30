package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.user.entity.UserEntity;

import java.util.UUID;

public record DoctorResposeDTO(
        UUID id,
        String specialty,
        String licenseNumber,
        Integer defaultConsultationDuration

) {
    public static DoctorResposeDTO fromEntity(DoctorEntity doctor, UserEntity userEntity) {

        return new DoctorResposeDTO(
                doctor.getId(),
                doctor.getSpecialty(),
                doctor.getLicenseNumber(),
                doctor.getDefaultConsultationDuration()
        );
    }
}
