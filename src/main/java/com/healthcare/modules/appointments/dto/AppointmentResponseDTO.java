package com.healthcare.modules.appointments.dto;

import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.modules.appointments.enums.AppointmentStatus;
import com.healthcare.modules.patients.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        String appointmentName,
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
        String notes,
        String medicalRecordNumber,
        DocumentType documentType,
        String patientFullName,
        String doctorFullName
) {

    public static AppointmentResponseDTO fromEntity(AppointmentEntity appointment) {

        String patientFullName = appointment.getPatient() != null
                ? appointment.getPatient().getFullName()
                : null;

        String doctorFullName = null;

        if (appointment.getDoctor() != null && appointment.getDoctor().getUser() != null) {
            doctorFullName = appointment.getDoctor().getUser().getUsername();
        }

        String medicalRecordNumber = appointment.getPatient() != null ? appointment.getPatient().getMedicalRecordNumber() : null;
        DocumentType documentType = appointment.getPatient() != null ? appointment.getPatient().getDocumentType() : null;

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentName(),
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
                appointment.getNotes(),
                medicalRecordNumber,
                documentType,
                patientFullName,
                doctorFullName
        );
    }

}
