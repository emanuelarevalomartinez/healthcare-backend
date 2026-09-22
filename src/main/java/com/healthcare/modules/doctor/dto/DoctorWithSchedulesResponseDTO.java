package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor_schedule.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedule.entity.DoctorScheduleEntity;

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
