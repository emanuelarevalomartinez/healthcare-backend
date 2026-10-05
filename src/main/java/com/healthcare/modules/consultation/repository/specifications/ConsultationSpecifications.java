package com.healthcare.modules.consultation.repository.specifications;

import com.healthcare.modules.consultation.entity.ConsultationEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ConsultationSpecifications {

    public static Specification<ConsultationEntity> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return criteriaBuilder.between(
                    root.get("consultationDate"),
                    startOfDay,
                    endOfDay
            );
        };
    }

    public static Specification<ConsultationEntity> hasCurrentUserId(UUID currentUserId) {
        return (root, query, criteriaBuilder) -> {
            if (currentUserId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> doctorJoin = root.join("createdByDoctor");
            Join<Object, Object> userJoin = doctorJoin.join("user");

            return criteriaBuilder.equal(
                    userJoin.get("id"),
                    currentUserId
            );
        };
    }

    public static Specification<ConsultationEntity> matchesDoctorOrPatientName(String term) {
        return (root, query, criteriaBuilder) -> {
            if (term == null || term.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + term.toLowerCase().trim() + "%";

            Join<Object, Object> appointmentJoin = root.join("appointment");
            Join<Object, Object> patientJoin = appointmentJoin.join("patient");
            Join<Object, Object> doctorJoin = appointmentJoin.join("doctor");
            Join<Object, Object> userJoin = doctorJoin.join("user");

            Predicate byPatient = criteriaBuilder.like(
                    criteriaBuilder.lower(patientJoin.get("fullName")),
                    pattern
            );

            Predicate byDoctor = criteriaBuilder.like(
                    criteriaBuilder.lower(userJoin.get("username")),
                    pattern
            );

            return criteriaBuilder.or(byPatient, byDoctor);
        };
    }

    public static Specification<ConsultationEntity> matchesClinicalText(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) return cb.conjunction();
            String pattern = "%" + term.toLowerCase().trim() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("diagnosis")), pattern),
                    cb.like(cb.lower(root.get("symptoms")), pattern)
            );
        };
    }

}
