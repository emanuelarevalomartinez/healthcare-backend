package com.healthcare.modules.consultation.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateConsultationDTO(

        @Size(max = 1000, message = "Los síntomas no pueden exceder 1000 caracteres")
        String symptoms,

        @Size(max = 2000, message = "El diagnóstico no puede exceder 2000 caracteres")
        String diagnosis,

        @Size(max = 2000, message = "El tratamiento no puede exceder 2000 caracteres")
        String treatment,

        @Size(max = 2000, message = "La receta no puede exceder 2000 caracteres")
        String prescription,

        @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
        String observations,

        LocalDateTime consultationDate,

        LocalDateTime nextReview

) {
}
