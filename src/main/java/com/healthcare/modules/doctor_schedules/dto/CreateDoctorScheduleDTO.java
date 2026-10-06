package com.healthcare.modules.doctor_schedules.dto;

import com.healthcare.modules.doctor_schedules.enums.DoctorScheduleDay;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record CreateDoctorScheduleDTO(

        @NotNull(message = "El médico es obligatorio")
        UUID doctorId,

        @NotNull(message = "Los horarios son obligatorios")
        @Valid
        List<DayScheduleDTO> schedules

) {

        public record DayScheduleDTO(

                @NotNull(message = "El día de la semana es obligatorio")
                DoctorScheduleDay dayOfWeek,

                @NotNull(message = "La hora de inicio es obligatoria")
                LocalTime startTime,

                @NotNull(message = "La hora de fin es obligatoria")
                LocalTime endTime,

                @NotNull(message = "El estado de disponibilidad es obligatorio")
                Boolean available,

                String notes

        ) {
        }
}
