package com.healthcare.modules.appointment.service;

import com.healthcare.modules.appointment.dto.AppointmentResponseDTO;
import com.healthcare.modules.appointment.dto.CreateAppointmentDTO;
import com.healthcare.modules.appointment.dto.UpdateAppointmentDTO;
import com.healthcare.modules.appointment.entity.AppointmentEntity;
import com.healthcare.modules.appointment.enums.AppointmentStatus;
import com.healthcare.modules.appointment.repository.AppointmentRepository;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.service.DoctorService;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.patient.service.PatientService;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.providers.CustomUserDetails;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(PatientService patientService, DoctorService doctorService, UserService userService, AppointmentRepository appointmentRepository) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponseDTO createAppointment(CreateAppointmentDTO createAppointmentDTO) {

        DoctorEntity doctorEntity = this.doctorService.findDoctorEntityById(createAppointmentDTO.doctorId());
        PatientEntity patientEntity = this.patientService.findPatientEntityById(createAppointmentDTO.patientId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userAuthenticatedDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userAuthenticatedDetails.getId();

        UserEntity userEntity = this.userService.findUserEntityById(userId);

        AppointmentEntity newAppointment = new AppointmentEntity();
        newAppointment.setPatient(patientEntity);
        newAppointment.setDoctor(doctorEntity);
        newAppointment.setAppointmentDateTime(createAppointmentDTO.appointmentDateTime());
        newAppointment.setDurationMinutes(createAppointmentDTO.durationMinutes());
        newAppointment.setConsultationReason(createAppointmentDTO.consultationReason());
        newAppointment.setStatus(AppointmentStatus.CONFIRMED);
        newAppointment.setCreatedBy(userEntity);
        newAppointment.setCreatedAt(LocalDateTime.now());

        if (createAppointmentDTO.notes() != null) {
            newAppointment.setNotes(createAppointmentDTO.notes());
        }

        AppointmentEntity saved = this.appointmentRepository.save(newAppointment);
        return AppointmentResponseDTO.fromEntity(saved);

    }

    public AppointmentResponseDTO updateAppointment(UUID id, UpdateAppointmentDTO dto) {
        try {
            AppointmentEntity appointment = this.findAppointmentEntityById(id);

            AppointmentStatus currentStatus = appointment.getStatus();
            AppointmentStatus newStatus = dto.status();

            validateStatusTransition(currentStatus, newStatus);

            switch (currentStatus) {

                case SCHEDULED -> {
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
                }
            }

            AppointmentEntity updated = appointmentRepository.save(appointment);
            return AppointmentResponseDTO.fromEntity(updated);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PageResponse<AppointmentResponseDTO> findAllAppointments(int page, int size) {
        try {

            Pageable pageable = PageRequest.of(page, size);
            Page<AppointmentEntity> result = appointmentRepository.findAllAppointmentsPaged(pageable);

            return new PageResponse<>(result.getContent().stream().map(AppointmentResponseDTO::fromEntity).toList(), result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public AppointmentResponseDTO findAppointmentById(UUID id) {
        try {

            AppointmentEntity findAppointmentById = this.appointmentRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorMessage.APPOINTMENT_NOT_FOUND_ID, ""));

            return AppointmentResponseDTO.fromEntity(findAppointmentById);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public AppointmentEntity findAppointmentEntityById(UUID id) {
        return this.appointmentRepository.findById(id).orElseThrow(() -> {
            return new ApplicationException(ErrorMessage.APPOINTMENT_NOT_FOUND_ID, id);
        });
    }

    @Override
    public void deleteAppointment(UUID id) {
        try {
            AppointmentEntity appointmentEntity = this.findAppointmentEntityById(id);
            appointmentRepository.deleteById(appointmentEntity.getId());

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
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
}
