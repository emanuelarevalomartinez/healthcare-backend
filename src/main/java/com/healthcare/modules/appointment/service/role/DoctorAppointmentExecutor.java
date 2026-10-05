package com.healthcare.modules.appointment.service.role;

import com.healthcare.modules.appointment.dto.AppointmentFilterParams;
import com.healthcare.modules.appointment.dto.AppointmentSearchParams;
import com.healthcare.modules.appointment.entity.AppointmentEntity;
import com.healthcare.modules.appointment.repository.specifications.AppointmentSpecifications;
import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DoctorAppointmentExecutor {

    private final AuthService authService;
    private final UserService userService;

    public DoctorAppointmentExecutor(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    public AppointmentSpecificationQuery findAppointmentsFilteredByDoctor(AppointmentFilterParams params) {
        UUID userId = authService.getCurrentUserId();
        UserEntity user = userService.findUserEntityById(userId);
        Specification<AppointmentEntity> spec = Specification
                .where(AppointmentSpecifications.hasDate(params.date()))
                .and(AppointmentSpecifications.hasPatientFullName(params.patientFullName()))
                .and(AppointmentSpecifications.hasDoctorUsername(user.getUsername()))
                .and(AppointmentSpecifications.hasPatientMedicalRecordNumber(params.patientMedicalRecordNumber()))
                .and(AppointmentSpecifications.hasPatientDocumentNumber(params.patientDocumentNumber()))
                .and(AppointmentSpecifications.hasDocumentType(params.patientDocumentType()))
                .and(AppointmentSpecifications.hasDoctorSpecialty(params.doctorSpecialty()))
                .and(AppointmentSpecifications.hasDoctorLicenseNumber(params.doctorLicenseNumber()))
                .and(AppointmentSpecifications.hasAppointmentStatus(params.appointmentStatus()));


        Sort sort = Sort.by(
                params.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                "appointmentDateTime"
        );

        Pageable pageable = PageRequest.of(params.page(), params.size(), sort);

        return new AppointmentSpecificationQuery(spec, pageable);
    }

    public AppointmentSpecificationQuery searchAppointmentsFilteredByDoctor(AppointmentSearchParams params) {
        UUID userId = authService.getCurrentUserId();
        UserEntity user = userService.findUserEntityById(userId);

        Specification<AppointmentEntity> spec = (root, query, cb) -> cb.conjunction();

        if (params.appointmentStatus() != null) {
            spec = spec.and(AppointmentSpecifications.hasAppointmentStatus(params.appointmentStatus()));
        }

        if (params.documentType() != null) {
            spec = spec.and(AppointmentSpecifications.hasDocumentType(params.documentType()));
        }

        if (params.searchTerm() != null && !params.searchTerm().trim().isEmpty()) {
            String term = params.searchTerm().trim();
            Specification<AppointmentEntity> searchSpec =
                    Specification.where(AppointmentSpecifications.hasPatientFullName(term))
                            .or(AppointmentSpecifications.hasPatientMedicalRecordNumber(term))
                            .and(AppointmentSpecifications.hasDoctorUsername(user.getUsername()));

            spec = spec.and(searchSpec);
        }
        Sort sort = Sort.by(params.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                "appointmentDateTime"
        );

        Pageable pageable = PageRequest.of(params.page(), params.size(), sort);
        return new AppointmentSpecificationQuery(spec, pageable);
    }

}
