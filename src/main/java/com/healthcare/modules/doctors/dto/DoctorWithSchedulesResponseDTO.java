package com.healthcare.modules.doctors.dto;

import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctor_schedules.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedules.entity.DoctorScheduleEntity;

import java.util.List;
import java.util.UUID;

public record DoctorWithSchedulesResponseDTO(
        UUID id,
        UUID modifiedBy,
        String specialty,
        String licenseNumber,
        Integer defaultConsultationDuration,
        List<DoctorScheduleResponseDTO> schedules
) {
    public static DoctorWithSchedulesResponseDTO fromEntity(DoctorEntity doctor, List<DoctorScheduleEntity> schedules) {

        return new DoctorWithSchedulesResponseDTO(
                doctor.getId(),
                doctor.getModifiedBy().getId(),
                doctor.getSpecialty(),
                doctor.getLicenseNumber(),
                doctor.getDefaultConsultationDuration(),
                schedules.stream()
                        .map(DoctorScheduleResponseDTO::fromEntity)
                        .toList()
        );
    }
}
