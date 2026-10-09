package com.healthcare.modules.consultations.service;

import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.modules.appointments.enums.AppointmentStatus;
import com.healthcare.modules.appointments.service.AppointmentService;
import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.consultations.dto.ConsultationResponseDTO;
import com.healthcare.modules.consultations.dto.CreateConsultationDTO;
import com.healthcare.modules.consultations.dto.UpdateConsultationDTO;
import com.healthcare.modules.consultations.entity.ConsultationEntity;
import com.healthcare.modules.consultations.repository.ConsultationRepository;
import com.healthcare.modules.consultations.repository.specifications.ConsultationSpecifications;
import com.healthcare.modules.consultations.service.role.ConsultationSpecificationQuery;
import com.healthcare.modules.consultations.service.role.DoctorConsultationExecutor;
import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctors.service.DoctorService;
import com.healthcare.modules.users.enums.UserRole;
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
import java.util.UUID;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ConsultationRepository consultationRepository;
    private final AuthService authService;
    private final DoctorConsultationExecutor doctorConsultationExecutor;

    public ConsultationServiceImpl(DoctorService doctorService, AppointmentService appointmentService, ConsultationRepository consultationRepository, AuthService authService, DoctorConsultationExecutor doctorConsultationExecutor) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.consultationRepository = consultationRepository;
        this.authService = authService;
        this.doctorConsultationExecutor = doctorConsultationExecutor;
    }

    @Override
    public ConsultationResponseDTO createConsultation(CreateConsultationDTO createConsultationDTO) {

        if (consultationRepository.existsByAppointmentId(createConsultationDTO.appointmentId())) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_ALREADY_EXISTS_FOR_APPOINTMENT, "");
        }

        DoctorEntity doctorEntity = this.doctorService.findDoctorEntityById(createConsultationDTO.createdByDoctor());

        AppointmentEntity appointmentEntity = this.appointmentService.findAppointmentEntityById(createConsultationDTO.appointmentId());

        if(appointmentEntity.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_APPOINTMENT_NOT_CONFIRMED, "");
        }

        if(!doctorEntity.getId().equals(appointmentEntity.getDoctor().getId())){
            throw new ApplicationException(ErrorMessage.CONSULTATION_DOCTOR_MISMATCH, "");
        }

        if (createConsultationDTO.consultationDate().isBefore(appointmentEntity.getAppointmentDateTime())) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_DATE_BEFORE_APPOINTMENT, "");
        }

        ConsultationEntity newConsultation = new ConsultationEntity();
        newConsultation.setAppointment(appointmentEntity);
        newConsultation.setConsultationName(createConsultationDTO.consultationName());
        newConsultation.setSymptoms(createConsultationDTO.symptoms());
        newConsultation.setDiagnosis(createConsultationDTO.diagnosis());
        newConsultation.setTreatment(createConsultationDTO.treatment());
        newConsultation.setPrescription(createConsultationDTO.prescription());
        newConsultation.setObservations(createConsultationDTO.observations());
        newConsultation.setConsultationDate(createConsultationDTO.consultationDate());
        newConsultation.setNextReview(createConsultationDTO.nextReview());
        newConsultation.setCreatedByDoctor(doctorEntity);
        newConsultation.setRegistrationDate(LocalDateTime.now());

        this.consultationRepository.save(newConsultation);
        this.appointmentService.markAsAttended(appointmentEntity.getId());

        return ConsultationResponseDTO.fromEntity(newConsultation);
    }

    @Override
    public ConsultationResponseDTO updateConsultation(UUID id, UpdateConsultationDTO updateConsultationDTO) {

        ConsultationEntity findConsultation = this.findConsultationEntityById(id);

        if(findConsultation.getAppointment().getStatus() != AppointmentStatus.ATTENDED) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_APPOINTMENT_NOT_ATTENDED, "");
        }

        if (updateConsultationDTO.consultationName() != null) {
            findConsultation.setConsultationName(updateConsultationDTO.consultationName());
        }

        if (updateConsultationDTO.symptoms() != null) {
            findConsultation.setSymptoms(updateConsultationDTO.symptoms());
        }
        if (updateConsultationDTO.diagnosis() != null) {
            findConsultation.setDiagnosis(updateConsultationDTO.diagnosis());
        }
        if (updateConsultationDTO.treatment() != null) {
            findConsultation.setTreatment(updateConsultationDTO.treatment());
        }
        if (updateConsultationDTO.prescription() != null) {
            findConsultation.setPrescription(updateConsultationDTO.prescription());
        }
        if (updateConsultationDTO.observations() != null) {
            findConsultation.setObservations(updateConsultationDTO.observations());
        }
        if (updateConsultationDTO.consultationDate() != null) {
            findConsultation.setConsultationDate(updateConsultationDTO.consultationDate());
        }
        if (updateConsultationDTO.nextReview() != null) {
            findConsultation.setNextReview(updateConsultationDTO.nextReview());
        }

        ConsultationEntity consultationUpdated = this.consultationRepository.save(findConsultation);

        return ConsultationResponseDTO.fromEntity(consultationUpdated);
    }

    @Override
    public PageResponse<ConsultationResponseDTO> findAllConsultations(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ConsultationEntity> result = consultationRepository.findAllConsultationsPaged(pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(ConsultationResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public PageResponse<ConsultationResponseDTO> findConsultationsFiltered(int page, int size, boolean ascending, LocalDate date) {

        ConsultationSpecificationQuery query;

        if (authService.getCurrentRole().equals(UserRole.DOCTOR)) {
            query = doctorConsultationExecutor.findConsultationsFilteredByDoctor(page, size, ascending, date);
        } else {
            query = buildDefaultFindConsultationFilteredQuery(page, size, ascending, date);
        }

        Page<ConsultationEntity> result = consultationRepository.findAll(
                query.specification(),
                query.pageable()
        );

        return new PageResponse<>(
                result.getContent().stream()
                        .map(ConsultationResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public PageResponse<ConsultationResponseDTO> searchConsultations(int page, int size, Boolean ascending, String searchTerm) {

        ConsultationSpecificationQuery query;

        if (authService.getCurrentRole().equals(UserRole.DOCTOR)) {
            query = doctorConsultationExecutor.searchConsultationsFilteredByDoctor(page, size, ascending, searchTerm);
        } else {
            query = buildDefaultFindConsultationsSearchQuery(page, size, ascending, searchTerm);
        }

        Page<ConsultationEntity> result = consultationRepository.findAll(
                query.specification(),
                query.pageable()
        );

        return new PageResponse<>(
                result.getContent().stream()
                        .map(ConsultationResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );

    }

    @Override
    public ConsultationResponseDTO findConsultationById(UUID id) {

        ConsultationEntity findConsultationById = this.consultationRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.CONSULTATION_NOT_FOUND_ID, "")
                );

        return ConsultationResponseDTO.fromEntity(findConsultationById);
    }

    @Override
    public ConsultationEntity findConsultationEntityById(UUID id) {
        return this.consultationRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.CONSULTATION_NOT_FOUND_ID, id);
                });
    }

    @Override
    public void deleteConsultation(UUID id) {
        ConsultationEntity consultation = this.findConsultationEntityById(id);
        consultationRepository.deleteById(consultation.getId());
    }

    private ConsultationSpecificationQuery buildDefaultFindConsultationFilteredQuery(int page, int size, boolean ascending, LocalDate date) {

        Specification<ConsultationEntity> spec = Specification
                .where(ConsultationSpecifications.hasDate(date));


        Sort sort = Sort.by(
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                "registrationDate"
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        return new ConsultationSpecificationQuery(spec, pageable);
    }

    private ConsultationSpecificationQuery buildDefaultFindConsultationsSearchQuery(int page, int size, boolean ascending, String searchTerm) {
        Specification<ConsultationEntity> spec = Specification
                .where(ConsultationSpecifications.matchesDoctorOrPatientName(searchTerm))
                .or(ConsultationSpecifications.matchesClinicalText(searchTerm));


        Sort sort = Sort.by(
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                "registrationDate"
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        return new ConsultationSpecificationQuery(spec, pageable);
    }
}
