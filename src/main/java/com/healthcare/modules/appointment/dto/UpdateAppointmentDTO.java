package com.healthcare.modules.appointment.dto;

import com.healthcare.modules.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateAppointmentDTO(

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
