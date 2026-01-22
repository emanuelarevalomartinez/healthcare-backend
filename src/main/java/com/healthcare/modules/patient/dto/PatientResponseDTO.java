package com.healthcare.modules.patient.dto;

import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.modules.patient.enums.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PatientResponseDTO(

        UUID id,
        String medicalRecordNumber,
        String fullName,
        DocumentType documentType,
        String documentNumber,
        LocalDate birthDate,
        Sex sex,
        String phone,
        String email,
        String address,
        String notes,
        UUID createdById,
        LocalDateTime createdAt

) {

    public static PatientResponseDTO fromEntity(PatientEntity patient) {

        return new PatientResponseDTO(
                patient.getId(),
                patient.getMedicalRecordNumber(),
                patient.getFullName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),
                patient.getBirthDate(),
                patient.getSex(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getNotes(),
                patient.getCreatedBy() != null ? patient.getCreatedBy().getId() : null,
                patient.getCreatedAt()
        );
    }

}

