package com.healthcare.modules.audit.dto;

import com.healthcare.modules.audit.enums.AuditAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateAuditDTO(

        @NotNull(message = "El ID de usuario es obligatorio")
        UUID userId,

        @NotNull(message = "La acción es obligatoria")
        AuditAction action,

        @NotBlank(message = "La tabla afectada es obligatoria")
        @Size(max = 200, message = "El nombre de la tabla afectada no puede exceder 200 caracteres")
        String affectedTable,

        @NotBlank(message = "El ID del registro es obligatorio")
        @Size(max = 100, message = "El ID del registro no puede exceder 100 caracteres")
        String recordId,

        @Size(max = 5000, message = "El valor antiguo no puede exceder 5000 caracteres")
        String oldValue,

        @Size(max = 5000, message = "El valor nuevo no puede exceder 5000 caracteres")
        String newValue

) {
}
