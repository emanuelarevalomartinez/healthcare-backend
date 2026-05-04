package com.healthcare.modules.appointment.dto;

import com.healthcare.modules.appointment.entity.AppointmentEntity;
import com.healthcare.modules.appointment.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        LocalDateTime appointmentDateTime,
        Integer durationMinutes,
        String consultationReason,
        AppointmentStatus status,
        UUID cancelledBy,
        String cancellationReason,
        UUID createdBy,
        LocalDateTime createdAt,
        LocalDateTime confirmedAt,
        LocalDateTime attendedAt,
        String notes
) {

    public static AppointmentResponseDTO fromEntity(AppointmentEntity appointment) {

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDateTime(),
                appointment.getDurationMinutes(),
                appointment.getConsultationReason(),
                appointment.getStatus(),
                appointment.getCancelledBy() != null ? appointment.getCancelledBy().getId() : null,
                appointment.getCancellationReason(),
                appointment.getCreatedBy() != null ? appointment.getCreatedBy().getId() : null,
                appointment.getCreatedAt(),
                appointment.getConfirmedAt(),
                appointment.getAttendedAt(),
                appointment.getNotes()
        );
    }

}
