package com.healthcare.modules.reminder.dto;

import com.healthcare.modules.reminder.enums.ReminderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateReminderDTO(

        @NotNull(message = "El ID del usuario es obligatorio")
        UUID userId,

        @NotNull(message = "El tipo de recordatorio es obligatorio")
        ReminderType type,

        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 500, message = "El mensaje no puede exceder 500 caracteres")
        String message,

        @NotNull(message = "La fecha del recordatorio es obligatoria")
        LocalDateTime reminderDate

) {
}
