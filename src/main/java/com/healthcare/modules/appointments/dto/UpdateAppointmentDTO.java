package com.healthcare.modules.appointments.dto;

import com.healthcare.modules.appointments.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateAppointmentDTO(

        @Size(max = 150, message = "El nombre de la cita no puede exceder 150 caracteres")
        String appointmentName,

        @Future(message = "La fecha y hora de la cita debe ser futura")
        LocalDateTime appointmentDateTime,

        @Positive(message = "La duración de la cita debe ser un número positivo")
        Integer durationMinutes,

        @Size(max = 255, message = "El motivo de consulta no puede exceder 255 caracteres")
        String consultationReason,

        AppointmentStatus status,

        @Size(max = 255, message = "El motivo de cancelación no puede exceder 255 caracteres")
        String cancellationReason,

        LocalDateTime confirmedAt,

        LocalDateTime attendedAt,

        @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
        String notes
) {
}
