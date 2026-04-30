package com.healthcare.modules.doctor.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateDoctorDTO(
        @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
        String specialty,

        @Size(max = 50, message = "La licencia no puede exceder 50 caracteres")
        String licenseNumber,

        @Positive(message = "La duración de la consulta debe ser un número positivo")
        Integer defaultConsultationDuration
) {
}
