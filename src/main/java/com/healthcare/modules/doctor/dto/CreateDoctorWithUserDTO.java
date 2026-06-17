package com.healthcare.modules.doctor.dto;

import com.healthcare.modules.user.enums.UserRole;
import jakarta.validation.constraints.*;

public record CreateDoctorWithUserDTO(

        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "El usuario solo puede contener letras, números y guiones bajos"
        )
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        String email,

        @NotNull(message = "El rol es obligatorio")
        UserRole role,

        @NotNull(message = "El estado activo es obligatorio")
        Boolean isActive,

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
