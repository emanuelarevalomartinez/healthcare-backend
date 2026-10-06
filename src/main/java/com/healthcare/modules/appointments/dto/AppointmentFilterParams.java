package com.healthcare.modules.appointments.dto;

import com.healthcare.modules.appointments.enums.AppointmentStatus;
import com.healthcare.modules.patients.enums.DocumentType;

import java.time.LocalDate;

public record AppointmentFilterParams(
        int page,
        int size,
        boolean ascending,
        LocalDate date,
        AppointmentStatus appointmentStatus,
        String patientFullName,
        String doctorUserName,
        String patientMedicalRecordNumber,
        DocumentType patientDocumentType,
        String patientDocumentNumber,
        String doctorSpecialty,
        String doctorLicenseNumber
) {}
