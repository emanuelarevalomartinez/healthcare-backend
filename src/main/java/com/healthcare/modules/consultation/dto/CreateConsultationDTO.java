package com.healthcare.modules.consultation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateConsultationDTO(

        @NotNull(message = "El ID de la cita es obligatorio")
        UUID appointmentId,

        @NotNull(message = "El ID del médico que crea es obligatorio")
        UUID createdByDoctor,

        @NotBlank(message = "Los síntomas son obligatorios")
        @Size(max = 1000, message = "Los síntomas no pueden exceder 1000 caracteres")
        String symptoms,

        @NotBlank(message = "El diagnóstico es obligatorio")
        @Size(max = 2000, message = "El diagnóstico no puede exceder 2000 caracteres")
        String diagnosis,

        @NotBlank(message = "El tratamiento es obligatorio")
        @Size(max = 2000, message = "El tratamiento no puede exceder 2000 caracteres")
        String treatment,

        @Size(max = 2000, message = "La receta no puede exceder 2000 caracteres")
        String prescription,

        @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
        String observations,

        @NotNull(message = "La fecha de consulta es obligatoria")
        LocalDateTime consultationDate,

        LocalDateTime nextReview

) {
}
