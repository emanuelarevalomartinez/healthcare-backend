package com.healthcare.modules.patients.repository.specifications;

import com.healthcare.modules.patients.entity.PatientEntity;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecifications {

    public static Specification<PatientEntity> search(String search) {
        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("fullName")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("medicalRecordNumber")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("documentNumber")),
                            pattern
                    )
            );
        };
    }

}
