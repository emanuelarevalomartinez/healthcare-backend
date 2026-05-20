package com.healthcare.modules.audit.dto;

import jakarta.validation.constraints.Size;

public record UpdateAuditDTO(

        @Size(max = 5000, message = "El valor antiguo no puede exceder 5000 caracteres")
        String oldValue,

        @Size(max = 5000, message = "El valor nuevo no puede exceder 5000 caracteres")
        String newValue

) {
}
