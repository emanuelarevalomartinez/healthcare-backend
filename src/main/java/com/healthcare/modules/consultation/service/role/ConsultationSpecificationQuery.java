package com.healthcare.modules.consultation.service.role;

import com.healthcare.modules.consultation.entity.ConsultationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public record ConsultationSpecificationQuery(Specification<ConsultationEntity> specification, Pageable pageable) {
}
