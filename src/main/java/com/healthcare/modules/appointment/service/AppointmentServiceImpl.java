package com.healthcare.modules.appointment.service;

import com.healthcare.modules.appointment.dto.AppointmentResponseDTO;
import com.healthcare.modules.appointment.dto.CreateAppointmentDTO;
import com.healthcare.modules.appointment.entity.AppointmentEntity;
import com.healthcare.modules.appointment.enums.AppointmentStatus;
import com.healthcare.modules.appointment.repository.AppointmentRepository;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.service.DoctorService;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.patient.service.PatientService;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.providers.CustomUserDetails;
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
}
