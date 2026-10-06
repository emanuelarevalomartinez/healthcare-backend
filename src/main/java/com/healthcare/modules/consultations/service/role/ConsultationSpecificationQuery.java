package com.healthcare.modules.consultations.service.role;

import com.healthcare.modules.consultations.entity.ConsultationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public record ConsultationSpecificationQuery(Specification<ConsultationEntity> specification, Pageable pageable) {
}
