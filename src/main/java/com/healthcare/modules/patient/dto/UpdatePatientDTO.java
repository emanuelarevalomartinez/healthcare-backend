package com.healthcare.modules.patient.dto;

import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.modules.patient.enums.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdatePatientDTO(

        @Size(max = 50, message = "The medical record number must not exceed 50 characters")
        String medicalRecordNumber,

        @Size(max = 150, message = "The full name must not exceed 150 characters")
        String fullName,

        DocumentType documentType,

        @Size(max = 50, message = "The document number must not exceed 50 characters")
        String documentNumber,

        LocalDate birthDate,

        Sex sex,

        @Size(max = 20, message = "The phone number must not exceed 20 characters")
        String phone,

        @Email(message = "The email must be valid")
        @Size(max = 100, message = "The email must not exceed 100 characters")
        String email,

        @Size(max = 255, message = "The address must not exceed 255 characters")
        String address,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes

) {
}
