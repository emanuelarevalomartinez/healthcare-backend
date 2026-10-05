package com.healthcare.modules.consultation.service.role;

import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.consultation.entity.ConsultationEntity;
import com.healthcare.modules.consultation.repository.specifications.ConsultationSpecifications;
import com.healthcare.modules.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class DoctorConsultationExecutor {

    private final AuthService authService;
    private final UserService userService;

    public DoctorConsultationExecutor(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    public ConsultationSpecificationQuery findConsultationsFilteredByDoctor(int page, int size, boolean ascending, LocalDate date) {
        UUID userId = authService.getCurrentUserId();
        Specification<ConsultationEntity> spec = Specification
                .where(ConsultationSpecifications.hasDate(date))
                .and(ConsultationSpecifications.hasCurrentUserId(userId));


        Sort sort = Sort.by(
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                "registrationDate"
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        return new ConsultationSpecificationQuery(spec, pageable);
    }

    public ConsultationSpecificationQuery searchConsultationsFilteredByDoctor(int page, int size, boolean ascending, String searchTerm) {
        UUID userId = authService.getCurrentUserId();
        Specification<ConsultationEntity> spec = Specification
                .where(ConsultationSpecifications.matchesDoctorOrPatientName(searchTerm))
                .or(ConsultationSpecifications.matchesClinicalText(searchTerm))
                .and(ConsultationSpecifications.hasCurrentUserId(userId));


        Sort sort = Sort.by(
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                "registrationDate"
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        return new ConsultationSpecificationQuery(spec, pageable);
    }

}
