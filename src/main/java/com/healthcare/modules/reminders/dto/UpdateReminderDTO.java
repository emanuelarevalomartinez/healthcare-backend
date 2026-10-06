package com.healthcare.modules.reminders.dto;

import com.healthcare.modules.reminders.enums.ReminderType;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateReminderDTO(

        @Size(max = 500, message = "El mensaje no puede exceder 500 caracteres")
        String message,

        ReminderType type,

        LocalDateTime reminderDate,

        Boolean isRead

) {
}
