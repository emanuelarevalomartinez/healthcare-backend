package com.healthcare.modules.consultation_histories.dto;

import com.healthcare.modules.consultation_histories.entity.ConsultationHistoriesEntity;
import com.healthcare.modules.consultation_histories.enums.ConsultationHistoriesEventType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultationHistoriesResponseDTO(
        UUID id,
        UUID consultationId,
        UUID appointmentId,
        UUID patientId,
        UUID doctorId,
        UUID performedByUserId,
        String consultationName,
        String appointmentName,
        String patientName,
        String doctorName,
        String performedByUserName,
        ConsultationHistoriesEventType eventType,
        LocalDateTime registeredAt
) {

    public static ConsultationHistoriesResponseDTO fromEntity(ConsultationHistoriesEntity entity) {
        return new ConsultationHistoriesResponseDTO(
                entity.getId(),
                entity.getConsultationId(),
                entity.getAppointmentId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getPerformedByUserId(),
                entity.getConsultationName(),
                entity.getAppointmentName(),
                entity.getPatientName(),
                entity.getDoctorName(),
                entity.getPerformedByUserName(),
                entity.getEventType(),
                entity.getRegisteredAt()
        );
    }

}
