package com.healthcare.modules.appointments.service.role;

import com.healthcare.modules.appointments.entity.AppointmentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public record AppointmentSpecificationQuery(Specification<AppointmentEntity> specification, Pageable pageable) {
}
