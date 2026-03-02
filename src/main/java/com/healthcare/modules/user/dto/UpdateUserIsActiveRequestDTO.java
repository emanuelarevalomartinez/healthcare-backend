package com.healthcare.modules.user.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateUserIsActiveRequestDTO(
        @NotNull(message = "El nuevo estado es obligatorio")
        Boolean isActive
) {
}
