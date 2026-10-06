package com.healthcare.modules.doctors.dto;

import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctor_schedules.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedules.entity.DoctorScheduleEntity;
import com.healthcare.modules.users.dto.UserResponseDTO;
import com.healthcare.modules.users.entity.UserEntity;

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
