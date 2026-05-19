package com.healthcare.modules.reminder.dto;

import com.healthcare.modules.reminder.entity.ReminderEntity;
import com.healthcare.modules.reminder.enums.ReminderType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReminderResponseDTO(
        UUID id,
        ReminderType type,
        String message,
        LocalDateTime reminderDate,
        Boolean isRead,
        LocalDateTime creationDate
) {

    public static ReminderResponseDTO fromEntity(ReminderEntity reminder) {

        return new ReminderResponseDTO(
                reminder.getId(),
                reminder.getType(),
                reminder.getMessage(),
                reminder.getReminderDate(),
                reminder.getRead(),
                reminder.getCreationDate()
        );
    }

}
