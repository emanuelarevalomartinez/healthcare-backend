package com.healthcare.modules.appointments.service.role;

import com.healthcare.modules.appointments.dto.AppointmentFilterParams;
import com.healthcare.modules.appointments.dto.AppointmentSearchParams;
import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.modules.appointments.repository.specifications.AppointmentSpecifications;
import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.users.entity.UserEntity;
import com.healthcare.modules.users.service.UserService;
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

        Specification<AppointmentEntity> spec;

        if (Boolean.TRUE.equals(params.searchByNameOnly())) {
            spec = Specification
                    .where(AppointmentSpecifications.hasDate(params.date()))
                    .and(AppointmentSpecifications.hasAppointmentName(params.appointmentName()))
                    .and(AppointmentSpecifications.hasDoctorUsername(user.getUsername()));
        } else {
            spec = Specification
                    .where(AppointmentSpecifications.hasDate(params.date()))
                    .and(AppointmentSpecifications.hasAppointmentName(params.appointmentName()))
                    .and(AppointmentSpecifications.hasPatientFullName(params.patientFullName()))
                    .and(AppointmentSpecifications.hasDoctorUsername(user.getUsername()))
                    .and(AppointmentSpecifications.hasPatientMedicalRecordNumber(params.patientMedicalRecordNumber()))
                    .and(AppointmentSpecifications.hasPatientDocumentNumber(params.patientDocumentNumber()))
                    .and(AppointmentSpecifications.hasDocumentType(params.patientDocumentType()))
                    .and(AppointmentSpecifications.hasDoctorSpecialty(params.doctorSpecialty()))
                    .and(AppointmentSpecifications.hasDoctorLicenseNumber(params.doctorLicenseNumber()))
                    .and(AppointmentSpecifications.hasAppointmentStatus(params.appointmentStatus()));
        }

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

            Specification<AppointmentEntity> searchSpec;

            if (Boolean.TRUE.equals(params.searchByNameOnly())) {
                searchSpec = Specification
                        .where(AppointmentSpecifications.hasAppointmentName(term))
                        .and(AppointmentSpecifications.hasDoctorUsername(user.getUsername()));
            } else {
                searchSpec = Specification.where(AppointmentSpecifications.hasPatientFullName(term))
                                .or(AppointmentSpecifications.hasAppointmentName(term))
                                .or(AppointmentSpecifications.hasPatientMedicalRecordNumber(term))
                                .and(AppointmentSpecifications.hasDoctorUsername(user.getUsername()));
            }

            spec = spec.and(searchSpec);
        }

        Sort sort = Sort.by(params.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                "appointmentDateTime"
        );

        Pageable pageable = PageRequest.of(params.page(), params.size(), sort);
        return new AppointmentSpecificationQuery(spec, pageable);
    }

}
