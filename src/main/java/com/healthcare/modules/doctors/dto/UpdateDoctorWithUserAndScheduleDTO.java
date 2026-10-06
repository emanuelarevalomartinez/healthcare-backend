package com.healthcare.modules.doctors.dto;

import com.healthcare.modules.doctor_schedules.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.users.dto.UpdateUserDTO;
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
