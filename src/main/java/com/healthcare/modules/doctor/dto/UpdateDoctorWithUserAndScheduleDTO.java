package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.doctor_schedule.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import jakarta.validation.Valid;


public record UpdateDoctorWithUserAndScheduleDTO(

        @Valid
        UpdateUserDTO user,

        @Valid
        UpdateDoctorDTO doctor,

        @Valid
        UpdateDoctorScheduleDTO schedule

) {
}
