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
        LocalDateTime registrationDate
) {

    public static ConsultationResponseDTO fromEntity(ConsultationEntity consultation) {

        return new ConsultationResponseDTO(
                consultation.getId(),
                consultation.getSymptoms(),
                consultation.getDiagnosis(),
                consultation.getTreatment(),
                consultation.getPrescription(),
                consultation.getObservations(),
                consultation.getConsultationDate(),
                consultation.getNextReview(),
                consultation.getRegistrationDate()
        );
    }

}
