package com.healthcare.modules.consultation_histories.dto;

import com.healthcare.modules.consultation_histories.enums.ConsultationHistoriesEventType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateConsultationHistoriesDTO(

        @NotNull(message = "El ID de la consulta es obligatorio")
        UUID consultationId,

        @NotNull(message = "El ID de la cita es obligatorio")
        UUID appointmentId,

        @NotNull(message = "El ID del paciente es obligatorio")
        UUID patientId,

        @NotNull(message = "El ID del médico es obligatorio")
        UUID doctorId,

        @NotNull(message = "El tipo de evento es obligatorio")
        ConsultationHistoriesEventType eventType

) {
}
