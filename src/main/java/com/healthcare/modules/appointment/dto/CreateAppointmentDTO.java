package com.healthcare.modules.appointment.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentDTO(

        @NotNull(message = "El ID del paciente es obligatorio")
        UUID patientId,

        @NotNull(message = "El ID del médico es obligatorio")
        UUID doctorId,

        @NotNull(message = "La fecha y hora de la cita es obligatoria")
        @Future(message = "La fecha y hora de la cita debe ser futura")
        LocalDateTime appointmentDateTime,

        @NotNull(message = "La duración de la cita es obligatoria")
        @Positive(message = "La duración de la cita debe ser un número positivo")
        Integer durationMinutes,

        @NotBlank(message = "El motivo de consulta es obligatorio")
        @Size(max = 255, message = "El motivo de consulta no puede exceder 255 caracteres")
        String consultationReason,

        @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
        String notes

) {
}
