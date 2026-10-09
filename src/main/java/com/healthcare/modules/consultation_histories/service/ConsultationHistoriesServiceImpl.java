package com.healthcare.modules.consultation_histories.service;

import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.modules.appointments.service.AppointmentService;
import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.consultation_histories.dto.ConsultationHistoryGroupResponseDTO;
import com.healthcare.modules.consultation_histories.repository.specifications.ConsultationHistoriesSpecifications;
import com.healthcare.modules.consultations.entity.ConsultationEntity;
import com.healthcare.modules.consultations.service.ConsultationService;
import com.healthcare.modules.consultation_histories.dto.ConsultationHistoriesResponseDTO;
import com.healthcare.modules.consultation_histories.dto.CreateConsultationHistoriesDTO;
import com.healthcare.modules.consultation_histories.entity.ConsultationHistoriesEntity;
import com.healthcare.modules.consultation_histories.repository.ConsultationHistoriesRepository;
import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctors.service.DoctorService;
import com.healthcare.modules.patients.entity.PatientEntity;
import com.healthcare.modules.patients.service.PatientService;
import com.healthcare.modules.users.entity.UserEntity;
import com.healthcare.modules.users.enums.UserRole;
import com.healthcare.modules.users.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsultationHistoriesServiceImpl implements ConsultationHistoriesService{

    private final ConsultationHistoriesRepository consultationHistoriesRepository;
    private final AuthService authService;
    private final UserService userService;
    private final ConsultationService consultationService;
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public ConsultationHistoriesServiceImpl(ConsultationHistoriesRepository consultationHistoriesRepository, AuthService authService, UserService userService, ConsultationService consultationService, AppointmentService appointmentService, PatientService patientService, DoctorService doctorService) {
        this.consultationHistoriesRepository = consultationHistoriesRepository;
        this.authService = authService;
        this.userService = userService;
        this.consultationService = consultationService;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @Override
    public ConsultationHistoriesResponseDTO createConsultationHistories(CreateConsultationHistoriesDTO createConsultationHistoriesDTO) {

        ConsultationEntity consultationEntity = this.consultationService.findConsultationEntityById(createConsultationHistoriesDTO.consultationId());
        AppointmentEntity appointmentEntity = this.appointmentService.findAppointmentEntityById(createConsultationHistoriesDTO.appointmentId());
        PatientEntity patientEntity = this.patientService.findPatientEntityById(createConsultationHistoriesDTO.patientId());
        DoctorEntity doctorEntity = this.doctorService.findDoctorEntityById(createConsultationHistoriesDTO.doctorId());

        UUID userId = authService.getCurrentUserId();
        UserEntity userEntity = this.userService.findUserEntityById(userId);

        ConsultationHistoriesEntity consultationHistoriesEntity = new ConsultationHistoriesEntity();

        consultationHistoriesEntity.setConsultationId(createConsultationHistoriesDTO.consultationId());
        consultationHistoriesEntity.setAppointmentId(createConsultationHistoriesDTO.appointmentId());
        consultationHistoriesEntity.setPatientId(createConsultationHistoriesDTO.patientId());
        consultationHistoriesEntity.setDoctorId(createConsultationHistoriesDTO.doctorId());

        consultationHistoriesEntity.setPerformedByUserId(userId);
        consultationHistoriesEntity.setConsultationName(consultationEntity.getConsultationName());
        consultationHistoriesEntity.setAppointmentName(appointmentEntity.getAppointmentName());
        consultationHistoriesEntity.setPatientName(patientEntity.getFullName());
        consultationHistoriesEntity.setDoctorName(doctorEntity.getUser().getUsername());
        consultationHistoriesEntity.setPerformedByUserName(userEntity.getUsername());

        consultationHistoriesEntity.setEventType(createConsultationHistoriesDTO.eventType());

        ConsultationHistoriesEntity consultationHistoriesSaved = this.consultationHistoriesRepository.save(consultationHistoriesEntity);

        return ConsultationHistoriesResponseDTO.fromEntity(consultationHistoriesSaved);

    }

    @Override
    public PageResponse<ConsultationHistoryGroupResponseDTO> findConsultationHistoriesGroupedByDate(
            int page,
            int size,
            boolean ascending
    ) {

        Sort sort = Sort.by(
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                "registeredAt"
        );

        Pageable pageable = PageRequest.of(page, size, sort);
       /* Specification<ConsultationHistoriesEntity> spec;

        if (authService.getCurrentRole().equals(UserRole.DOCTOR)) {
            UUID userId = authService.getCurrentUserId();
            DoctorEntity doctor = doctorService.findDoctorEntityByUserId(userId);
            spec = ConsultationHistoriesSpecifications.hasDoctorId(doctor.getId());
        } else {
            spec = null;
        }*/

        Specification<ConsultationHistoriesEntity> spec = Specification.unrestricted();

        if (authService.getCurrentRole().equals(UserRole.DOCTOR)) {
            UUID userId = authService.getCurrentUserId();
            DoctorEntity doctor = doctorService.findDoctorEntityByUserId(userId);
            spec = spec.and(ConsultationHistoriesSpecifications.hasDoctorId(doctor.getId()));
        }

        Page<ConsultationHistoriesEntity> result = consultationHistoriesRepository.findAll(spec, pageable);

        Map<LocalDate, List<ConsultationHistoriesEntity>> grouped = result.getContent().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getRegisteredAt().toLocalDate(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<ConsultationHistoryGroupResponseDTO> groups = grouped.entrySet().stream()
                .map(entry -> new ConsultationHistoryGroupResponseDTO(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(ConsultationHistoriesResponseDTO::fromEntity)
                                .toList()
                ))
                .toList();

        return new PageResponse<>(
                groups,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public void deleteConsultationHistoryById(UUID id) {
        ConsultationHistoriesEntity history = this.consultationHistoriesRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.CONSULTATION_HISTORY_NOT_FOUND_ID, id));
        this.consultationHistoriesRepository.delete(history);
    }

    @Override
    public long deleteConsultationHistoriesByDate(LocalDate date) {
        if (date == null) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_HISTORY_INVALID_DATE, "");
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        long deleted = this.consultationHistoriesRepository.deleteByRegisteredAtBetween(startOfDay, endOfDay);

        if (deleted == 0) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_HISTORY_NOT_FOUND_BY_DATE, date.toString());
        }

        return deleted;
    }

    @Override
    public long deleteAllConsultationHistories() {
        long total = this.consultationHistoriesRepository.count();

        if (total == 0) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_HISTORY_EMPTY, "");
        }

        this.consultationHistoriesRepository.deleteAll();
        return total;
    }

}
