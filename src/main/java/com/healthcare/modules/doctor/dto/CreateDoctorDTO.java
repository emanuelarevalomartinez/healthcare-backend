package com.healthcare.modules.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDoctorDTO(

        @NotNull(message = "El ID de usuario es obligatorio")
        UUID userId,

        @NotBlank(message = "El nombre de la especialidad es obligatorio")
        @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
        String specialty,

        @NotBlank(message = "El número de licencia es obligatorio")
        @Size(max = 50, message = "La licencia no puede exceder 50 caracteres")
        String licenseNumber,

        @NotNull(message = "La duración de la consulta es obligatoria")
        @Positive(message = "La duración de la consulta debe ser un número positivo")
        Integer defaultConsultationDuration

) {
}
