package com.healthcare.modules.consultations.dto;

import com.healthcare.modules.consultations.entity.ConsultationEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultationResponseDTO(
        UUID id,
        String consultationName,
        String symptoms,
        String diagnosis,
        String treatment,
        String prescription,
        String observations,
        LocalDateTime consultationDate,
        LocalDateTime nextReview,
        LocalDateTime registrationDate,
        String appointmentName,
        LocalDateTime appointmentDateTime,
        String doctorName,
        String patientName
) {

    public static ConsultationResponseDTO fromEntity(ConsultationEntity consultation) {

        String appointmentName = null;
        LocalDateTime appointmentDateTime = null;
        String doctorName = null;
        String patientName = null;

        if (consultation.getAppointment() != null) {
            appointmentName = consultation.getAppointment().getAppointmentName();
            appointmentDateTime = consultation.getAppointment().getAppointmentDateTime();
            if (consultation.getAppointment().getDoctor() != null && consultation.getAppointment().getDoctor().getUser() != null) {
                doctorName = consultation.getAppointment().getDoctor().getUser().getUsername();
            }
            if (consultation.getAppointment().getPatient() != null) {
                patientName = consultation.getAppointment().getPatient().getFullName();
            }
        }

        return new ConsultationResponseDTO(
                consultation.getId(),
                consultation.getConsultationName(),
                consultation.getSymptoms(),
                consultation.getDiagnosis(),
                consultation.getTreatment(),
                consultation.getPrescription(),
                consultation.getObservations(),
                consultation.getConsultationDate(),
                consultation.getNextReview(),
                consultation.getRegistrationDate(),
                appointmentName,
                appointmentDateTime,
                doctorName,
                patientName
        );
    }

}
