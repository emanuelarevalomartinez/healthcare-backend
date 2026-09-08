package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor_schedule.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedule.entity.DoctorScheduleEntity;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;

import java.util.List;

public record DoctorWithUserAndScheduleResponseDTO(
        UserResponseDTO user,
        DoctorResponseDTO doctor,
        List<DoctorScheduleResponseDTO> schedules
) {

    public static DoctorWithUserAndScheduleResponseDTO fromEntities(
            UserEntity user,
            DoctorEntity doctor,
            List<DoctorScheduleEntity> schedules
    ) {
        return new DoctorWithUserAndScheduleResponseDTO(
                UserResponseDTO.fromEntity(user),
                DoctorResponseDTO.fromEntity(doctor),
                schedules.stream()
                        .map(DoctorScheduleResponseDTO::fromEntity)
                        .toList()
        );
    }
}
