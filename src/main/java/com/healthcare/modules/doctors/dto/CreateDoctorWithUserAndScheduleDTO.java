package com.healthcare.modules.doctors.dto;

import com.healthcare.modules.doctor_schedules.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.users.dto.CreateUserDTO;
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
