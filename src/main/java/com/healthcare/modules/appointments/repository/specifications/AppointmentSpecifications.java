package com.healthcare.modules.appointments.repository.specifications;

import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.modules.appointments.enums.AppointmentStatus;
import com.healthcare.modules.patients.enums.DocumentType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentSpecifications {

    public static Specification<AppointmentEntity> hasAppointmentName(String appointmentName) {
        return (root, query, criteriaBuilder) -> {
            if (appointmentName == null || appointmentName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("appointmentName")),
                    "%" + appointmentName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return criteriaBuilder.between(
                    root.get("appointmentDateTime"),
                    startOfDay,
                    endOfDay
            );
        };
    }

    public static Specification<AppointmentEntity> hasPatientFullName(String patientFullName) {
        return (root, query, criteriaBuilder) -> {
            if (patientFullName == null || patientFullName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> patientJoin = root.join("patient");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(patientJoin.get("fullName")),
                    "%" + patientFullName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasPatientMedicalRecordNumber(String medicalRecordNumber) {
        return (root, query, criteriaBuilder) -> {
            if (medicalRecordNumber == null || medicalRecordNumber.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> patientJoin = root.join("patient");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(patientJoin.get("medicalRecordNumber")),
                    "%" + medicalRecordNumber.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasPatientDocumentNumber(String documentNumber) {
        return (root, query, criteriaBuilder) -> {
            if (documentNumber == null || documentNumber.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> patientJoin = root.join("patient");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(patientJoin.get("documentNumber")),
                    "%" + documentNumber.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasDocumentType(DocumentType documentType) {
        return (root, query, criteriaBuilder) -> {
            if (documentType == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> patientJoin = root.join("patient");
            return criteriaBuilder.equal(
                    patientJoin.get("documentType"),
                    documentType
            );
        };
    }

    public static Specification<AppointmentEntity> hasDoctorUsername(String doctorUsername) {
        return (root, query, criteriaBuilder) -> {
            if (doctorUsername == null || doctorUsername.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> doctorJoin = root.join("doctor");
            Join<Object, Object> userJoin = doctorJoin.join("user");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(userJoin.get("username")),
                    "%" + doctorUsername.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasDoctorSpecialty(String specialty) {
        return (root, query, criteriaBuilder) -> {
            if (specialty == null || specialty.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> doctorJoin = root.join("doctor");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(doctorJoin.get("specialty")),
                    "%" + specialty.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasDoctorLicenseNumber(String licenseNumber) {
        return (root, query, criteriaBuilder) -> {
            if (licenseNumber == null || licenseNumber.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> doctorJoin = root.join("doctor");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(doctorJoin.get("licenseNumber")),
                    "%" + licenseNumber.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AppointmentEntity> hasAppointmentStatus(AppointmentStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
