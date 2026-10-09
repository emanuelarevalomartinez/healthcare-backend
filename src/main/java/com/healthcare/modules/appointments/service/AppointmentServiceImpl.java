package com.healthcare.modules.appointments.service;

import com.healthcare.modules.appointments.dto.*;
import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.modules.appointments.enums.AppointmentStatus;
import com.healthcare.modules.appointments.repository.AppointmentRepository;
import com.healthcare.modules.appointments.repository.specifications.AppointmentSpecifications;
import com.healthcare.modules.appointments.service.role.AppointmentSpecificationQuery;
import com.healthcare.modules.appointments.service.role.DoctorAppointmentExecutor;
import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctors.service.DoctorService;
import com.healthcare.modules.doctor_schedules.entity.DoctorScheduleEntity;
import com.healthcare.modules.doctor_schedules.enums.DoctorScheduleDay;
import com.healthcare.modules.doctor_schedules.service.DoctorScheduleService;
import com.healthcare.modules.patients.entity.PatientEntity;
import com.healthcare.modules.patients.service.PatientService;
import com.healthcare.modules.users.entity.UserEntity;
import com.healthcare.modules.users.enums.UserRole;
import com.healthcare.modules.users.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final AuthService authService;
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorAppointmentExecutor doctorAppointmentExecutor;

    public AppointmentServiceImpl(PatientService patientService, DoctorService doctorService, UserService userService, AppointmentRepository appointmentRepository, AuthService authService, DoctorScheduleService doctorScheduleService, DoctorAppointmentExecutor doctorAppointmentExecutor) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
        this.authService = authService;
        this.doctorScheduleService = doctorScheduleService;
        this.doctorAppointmentExecutor = doctorAppointmentExecutor;
    }

    @Override
    public AppointmentResponseDTO createAppointment(CreateAppointmentDTO createAppointmentDTO) {

        DoctorEntity doctorEntity = this.doctorService.findDoctorEntityById(createAppointmentDTO.doctorId());
        PatientEntity patientEntity = this.patientService.findPatientEntityById(createAppointmentDTO.patientId());

        UUID userId = authService.getCurrentUserId();
        UserEntity userEntity = this.userService.findUserEntityById(userId);

        validateDoctorOwnership(userEntity, doctorEntity.getId());

        LocalDateTime appointmentStart = createAppointmentDTO.appointmentDateTime();
        LocalDateTime appointmentEnd = appointmentStart.plusMinutes(createAppointmentDTO.durationMinutes());

        if (!appointmentStart.toLocalDate().equals(appointmentEnd.toLocalDate())) {
            throw new ApplicationException(ErrorMessage.APPOINTMENT_CROSSES_MIDNIGHT, "");
        }

        validateDoctorSchedule(doctorEntity.getId(), appointmentStart, appointmentEnd);

        boolean doctorHasConflict = this.appointmentRepository.existsDoctorConflict(
                doctorEntity.getId(),
                appointmentStart,
                appointmentEnd
        );

        if (doctorHasConflict) {
            throw new ApplicationException(ErrorMessage.APPOINTMENT_DOCTOR_SCHEDULE_CONFLICT, "");
        }

        boolean patientHasConflict = this.appointmentRepository.existsPatientConflict(
                patientEntity.getId(),
                appointmentStart,
                appointmentEnd
        );

        if (patientHasConflict) {
            throw new ApplicationException(ErrorMessage.APPOINTMENT_PATIENT_SCHEDULE_CONFLICT, "");
        }

        AppointmentEntity newAppointment = new AppointmentEntity();
        newAppointment.setPatient(patientEntity);
        newAppointment.setDoctor(doctorEntity);
        newAppointment.setAppointmentName(createAppointmentDTO.appointmentName());
        newAppointment.setAppointmentDateTime(createAppointmentDTO.appointmentDateTime());
        newAppointment.setDurationMinutes(createAppointmentDTO.durationMinutes());
        newAppointment.setConsultationReason(createAppointmentDTO.consultationReason());
        newAppointment.setStatus(AppointmentStatus.SCHEDULED);
        newAppointment.setCreatedBy(userEntity);
        newAppointment.setCreatedAt(LocalDateTime.now());

        if (createAppointmentDTO.notes() != null) {
            newAppointment.setNotes(createAppointmentDTO.notes());
        }

        AppointmentEntity saved = this.appointmentRepository.save(newAppointment);
        return AppointmentResponseDTO.fromEntity(saved);

    }

    public AppointmentResponseDTO updateAppointment(UUID id, UpdateAppointmentDTO dto) {

        AppointmentEntity appointment = this.findAppointmentEntityById(id);
        DoctorEntity doctorEntity = this.doctorService.findDoctorEntityById(appointment.getDoctor().getId());

        UUID userId = authService.getCurrentUserId();
        UserEntity userEntity = this.userService.findUserEntityById(userId);

        validateDoctorOwnership(userEntity, doctorEntity.getId());

        AppointmentStatus currentStatus = appointment.getStatus();
        AppointmentStatus newStatus = dto.status();

        validateStatusTransition(currentStatus, newStatus);

        switch (currentStatus) {

            case SCHEDULED -> {
                if (dto.appointmentName() != null) {
                    appointment.setAppointmentName(dto.appointmentName());
                }
                if (dto.appointmentDateTime() != null) {
                    appointment.setAppointmentDateTime(dto.appointmentDateTime());
                }

                if (dto.durationMinutes() != null) {
                    appointment.setDurationMinutes(dto.durationMinutes());
                }

                if (dto.consultationReason() != null) {
                    appointment.setConsultationReason(dto.consultationReason());
                }

                if (dto.notes() != null) {
                    appointment.setNotes(dto.notes());
                }
            }

            case CONFIRMED, ATTENDED -> {
                if (dto.appointmentName() != null) {
                    appointment.setAppointmentName(dto.appointmentName());
                }
                if (dto.notes() != null) {
                    appointment.setNotes(dto.notes());
                }
            }

            case CANCELLED, NO_SHOW -> {
                throw new ApplicationException(ErrorMessage.APPOINTMENT_FINAL_STATUS, "");
            }
        }

        if (newStatus != null) {
            appointment.setStatus(newStatus);

            if (newStatus == AppointmentStatus.CONFIRMED) {
                appointment.setConfirmedAt(LocalDateTime.now());
            }

            if (newStatus == AppointmentStatus.ATTENDED) {
                appointment.setAttendedAt(LocalDateTime.now());
            }

            if (newStatus == AppointmentStatus.CANCELLED) {
                appointment.setCancellationReason(dto.cancellationReason());
                appointment.setCancelledBy(userEntity);
            }
        }

        AppointmentEntity updated = appointmentRepository.save(appointment);
        return AppointmentResponseDTO.fromEntity(updated);
    }

    @Override
    public PageResponse<AppointmentResponseDTO> findAllAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> result = appointmentRepository.findAllAppointmentsPaged(pageable);

        return new PageResponse<>(result.getContent().stream().map(AppointmentResponseDTO::fromEntity).toList(), result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    @Override
    public AppointmentResponseDTO findAppointmentById(UUID id) {
        AppointmentEntity findAppointmentById = this.appointmentRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorMessage.APPOINTMENT_NOT_FOUND_ID, ""));

        return AppointmentResponseDTO.fromEntity(findAppointmentById);

    }

    @Override
    public PageResponse<AppointmentResponseDTO> findAppointmentsFiltered(
            AppointmentFilterParams params
    ) {
        AppointmentSpecificationQuery query;

        if (authService.getCurrentRole().equals(UserRole.DOCTOR)) {
            query = doctorAppointmentExecutor.findAppointmentsFilteredByDoctor(params);
        } else {
            query = buildDefaultFindAppointmentFilteredQuery(params);
        }

        Page<AppointmentEntity> result = appointmentRepository.findAll(
                query.specification(),
                query.pageable()
        );

        return new PageResponse<>(
                result.getContent().stream()
                        .map(AppointmentResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public PageResponse<AppointmentResponseDTO> searchAppointments(
            AppointmentSearchParams params
    ) {
        AppointmentSpecificationQuery query;

        if (authService.getCurrentRole().equals(UserRole.DOCTOR)) {
            query = doctorAppointmentExecutor.searchAppointmentsFilteredByDoctor(params);
        } else {
            query = buildDefaultFindAppointmentSearchQuery(params);
        }

        Page<AppointmentEntity> result = appointmentRepository.findAll(
                query.specification(),
                query.pageable()
        );

        return new PageResponse<>(
                result.getContent().stream()
                        .map(AppointmentResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }


    @Override
    public AppointmentEntity findAppointmentEntityById(UUID id) {
        return this.appointmentRepository.findById(id).orElseThrow(() -> {
            return new ApplicationException(ErrorMessage.APPOINTMENT_NOT_FOUND_ID, id);
        });
    }

    @Override
    public void deleteAppointment(UUID id) {
        AppointmentEntity appointmentEntity = this.findAppointmentEntityById(id);
        appointmentRepository.deleteById(appointmentEntity.getId());
    }

    private void validateStatusTransition(AppointmentStatus current, AppointmentStatus next) {
        if (next == null) return;

        boolean valid = (
                current == AppointmentStatus.SCHEDULED &&
                        (next == AppointmentStatus.CONFIRMED || next == AppointmentStatus.CANCELLED))
                || (current == AppointmentStatus.CONFIRMED && (next == AppointmentStatus.ATTENDED || next == AppointmentStatus.CANCELLED || next == AppointmentStatus.NO_SHOW)
        );

        if (!valid && current != next) {
            throw new ApplicationException(ErrorMessage.APPOINTMENT_INVALID_STATUS_TRANSITION, "");
        }
    }

    @Transactional
    public void markAsAttended(UUID appointmentId) {
        AppointmentEntity appointment = findAppointmentEntityById(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new ApplicationException(
                    ErrorMessage.APPOINTMENT_INVALID_STATUS_TRANSITION, ""
            );
        }

        appointment.setStatus(AppointmentStatus.ATTENDED);
        appointment.setAttendedAt(LocalDateTime.now());

        appointmentRepository.save(appointment);
    }

    private AppointmentSpecificationQuery buildDefaultFindAppointmentFilteredQuery(
            AppointmentFilterParams params
    ) {

        Specification<AppointmentEntity> spec;

        if (Boolean.TRUE.equals(params.searchByNameOnly())) {
            spec = Specification
                    .where(AppointmentSpecifications.hasDate(params.date()))
                    .and(AppointmentSpecifications.hasAppointmentName(params.appointmentName()));
        } else {
            spec = Specification
                    .where(AppointmentSpecifications.hasDate(params.date()))
                    .and(AppointmentSpecifications.hasAppointmentName(params.appointmentName()))
                    .and(AppointmentSpecifications.hasPatientFullName(params.patientFullName()))
                    .and(AppointmentSpecifications.hasDoctorUsername(params.doctorUserName()))
                    .and(AppointmentSpecifications.hasPatientMedicalRecordNumber(params.patientMedicalRecordNumber()))
                    .and(AppointmentSpecifications.hasPatientDocumentNumber(params.patientDocumentNumber()))
                    .and(AppointmentSpecifications.hasDocumentType(params.patientDocumentType()))
                    .and(AppointmentSpecifications.hasDoctorSpecialty(params.doctorSpecialty()))
                    .and(AppointmentSpecifications.hasDoctorLicenseNumber(params.doctorLicenseNumber()))
                    .and(AppointmentSpecifications.hasAppointmentStatus(params.appointmentStatus()));
        }

        Sort sort = Sort.by(params.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                "appointmentDateTime"
        );

        Pageable pageable = PageRequest.of(params.page(), params.size(), sort);
        return new AppointmentSpecificationQuery(spec, pageable);
    }

    private AppointmentSpecificationQuery buildDefaultFindAppointmentSearchQuery(
            AppointmentSearchParams params) {
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
                searchSpec = Specification.where(AppointmentSpecifications.hasAppointmentName(term));
            } else {
                searchSpec = Specification.where(AppointmentSpecifications.hasPatientFullName(term))
                                .or(AppointmentSpecifications.hasAppointmentName(term))
                                .or(AppointmentSpecifications.hasPatientMedicalRecordNumber(term))
                                .or(AppointmentSpecifications.hasDoctorUsername(term));
            }
            spec = spec.and(searchSpec);
        }

        Sort sort = Sort.by(params.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                "appointmentDateTime"
        );

        Pageable pageable = PageRequest.of(params.page(), params.size(), sort);
        return new AppointmentSpecificationQuery(spec, pageable);
    }

    private void validateDoctorSchedule(UUID doctorId,
                                        LocalDateTime appointmentStart,
                                        LocalDateTime appointmentEnd) {

        DoctorScheduleDay day = DoctorScheduleDay.fromDayOfWeek(appointmentStart.getDayOfWeek());
        DoctorScheduleEntity schedule = this.doctorScheduleService.findDoctorScheduleByDoctorAndDayOfWeek(doctorId, day);

        if (!schedule.isAvailable()) {
            throw new ApplicationException(
                    ErrorMessage.APPOINTMENT_DOCTOR_SCHEDULE_NOT_AVAILABLE, "");
        }

        LocalTime startTime = appointmentStart.toLocalTime();
        LocalTime endTime = appointmentEnd.toLocalTime();

        boolean insideSchedule = !startTime.isBefore(schedule.getStartTime()) && !endTime.isAfter(schedule.getEndTime());

        if (!insideSchedule) {
            throw new ApplicationException(
                    ErrorMessage.APPOINTMENT_OUTSIDE_DOCTOR_SCHEDULE, "Horario del doctor: " + schedule.getStartTime() + " - " + schedule.getEndTime());
        }
    }

    private void validateDoctorOwnership(UserEntity userEntity, UUID targetDoctorId) {

        if (userEntity.getRole() != UserRole.DOCTOR) {
            return;
        }

        if (userEntity.getDoctor() == null) {
            throw new ApplicationException(
                    ErrorMessage.APPOINTMENT_DOCTOR_PROFILE_MISSING,
                    userEntity.getId().toString()
            );
        }

        if (!userEntity.getDoctor().getId().equals(targetDoctorId)) {
            throw new ApplicationException(
                    ErrorMessage.APPOINTMENT_DOCTOR_NOT_ALLOWED,
                    userEntity.getId().toString()
            );
        }
    }
}
