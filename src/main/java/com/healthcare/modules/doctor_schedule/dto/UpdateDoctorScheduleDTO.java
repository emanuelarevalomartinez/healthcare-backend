package com.healthcare.modules.doctor_schedule.dto;

import com.healthcare.modules.doctor_schedule.enums.DoctorScheduleDay;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record UpdateDoctorScheduleDTO(

        @NotNull(message = "Los horarios son obligatorios")
        @Valid
        List<DayScheduleDTO> schedules

) {

        public record DayScheduleDTO(

                UUID id,

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
