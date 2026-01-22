package com.healthcare.modules.patient.dto;

import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.modules.patient.enums.Sex;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePatientDTO(

        @NotBlank(message = "El número de expediente es obligatorio")
        @Size(max = 50, message = "El número de expediente no puede exceder 50 caracteres")
        String medicalRecordNumber,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 150, message = "El nombre completo debe tener entre 3 y 150 caracteres")
        String fullName,

        @NotNull(message = "El tipo de documento es obligatorio")
        DocumentType documentType,

        @NotBlank(message = "El número de documento es obligatorio")
        @Size(max = 50, message = "El número de documento no puede exceder 50 caracteres")
        String documentNumber,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
        LocalDate birthDate,

        @NotNull(message = "El sexo es obligatorio")
        Sex sex,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
        @Pattern(
                regexp = "^[0-9+()\\-\\s]+$",
                message = "El teléfono contiene caracteres no válidos"
        )
        String phone,

        @Email(message = "El email debe ser válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        String email,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
        String address,

        @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
        String notes,

        @NotNull(message = "El usuario creador es obligatorio")
        UUID createdById

) { }
