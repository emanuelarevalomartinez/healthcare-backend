package com.healthcare.modules.consultation_histories.repository.specifications;

import com.healthcare.modules.consultation_histories.entity.ConsultationHistoriesEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ConsultationHistoriesSpecifications {

    public static Specification<ConsultationHistoriesEntity> hasDoctorId(UUID doctorId) {
        return (root, query, cb) -> cb.equal(root.get("doctorId"), doctorId);
    }
}
