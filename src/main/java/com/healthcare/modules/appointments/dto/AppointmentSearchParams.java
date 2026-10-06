package com.healthcare.modules.appointments.dto;

import com.healthcare.modules.appointments.enums.AppointmentStatus;
import com.healthcare.modules.patients.enums.DocumentType;

public record AppointmentSearchParams(
        int page,
        int size,
        boolean ascending,
        String searchTerm,
        AppointmentStatus appointmentStatus,
        DocumentType documentType
) {
}
