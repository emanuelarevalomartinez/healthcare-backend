package com.healthcare.modules.user.dto;

import com.healthcare.modules.user.enums.UserRole;
import jakarta.validation.constraints.*;

public record UpdateUserDTO(

        @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "El usuario solo puede contener letras, números y guiones bajos"
        )
        String username,

        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password,

        @Email(message = "El email debe ser válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        String email,

        UserRole role,

        Boolean isActive
) { }
