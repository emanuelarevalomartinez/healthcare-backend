package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor_schedule.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.user.dto.CreateUserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateDoctorWithUserAndScheduleDTO(

        @Valid
        @NotNull(message = "Los datos del usuario son obligatorios")
        CreateUserDTO user,

        @Valid
        @NotNull(message = "Los datos del médico son obligatorios")
        CreateDoctorWithoutUserDTO doctor,

        @Valid
        @NotNull(message = "Los horarios son obligatorios")
        ScheduleInfo schedule

) {
    public record ScheduleInfo(
            @NotNull(message = "Los horarios son obligatorios")
            @Valid
            List<CreateDoctorScheduleDTO.DayScheduleDTO> schedules
    ) {}
}
