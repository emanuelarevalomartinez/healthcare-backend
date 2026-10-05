package com.healthcare.modules.consultation.dto;

import com.healthcare.modules.consultation.entity.ConsultationEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultationResponseDTO(
        UUID id,
        String symptoms,
        String diagnosis,
        String treatment,
        String prescription,
        String observations,
        LocalDateTime consultationDate,
        LocalDateTime nextReview,
        LocalDateTime registrationDate,
        String doctorName,
        String patientName
) {

    public static ConsultationResponseDTO fromEntity(ConsultationEntity consultation) {

        String doctorName = null;
        String patientName = null;

        if (consultation.getAppointment() != null) {
            if (consultation.getAppointment().getDoctor() != null && consultation.getAppointment().getDoctor().getUser() != null) {
                doctorName = consultation.getAppointment().getDoctor().getUser().getUsername();
            }
            if (consultation.getAppointment().getPatient() != null) {
                patientName = consultation.getAppointment().getPatient().getFullName();
            }
        }

        return new ConsultationResponseDTO(
                consultation.getId(),
                consultation.getSymptoms(),
                consultation.getDiagnosis(),
                consultation.getTreatment(),
                consultation.getPrescription(),
                consultation.getObservations(),
                consultation.getConsultationDate(),
                consultation.getNextReview(),
                consultation.getRegistrationDate(),
                doctorName,
                patientName
        );
    }

}
