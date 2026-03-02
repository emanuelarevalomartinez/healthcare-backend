package com.healthcare.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserPasswordRequestDTO(
        @NotBlank(message = "La contraseña previa es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña previa contiene entre 8 y 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "La contraseña previa contiene al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String previousPassword,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La nueva contraseña debe tener entre 8 y 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "La nueva contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String newPassword
){}
