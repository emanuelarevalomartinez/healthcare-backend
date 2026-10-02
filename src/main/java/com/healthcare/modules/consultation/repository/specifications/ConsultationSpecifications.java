package com.healthcare.modules.consultation.repository.specifications;

import com.healthcare.modules.consultation.entity.ConsultationEntity;
import jakarta.persistence.criteria.Join;
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

}
