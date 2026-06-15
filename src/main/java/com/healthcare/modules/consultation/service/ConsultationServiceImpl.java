package com.healthcare.modules.consultation.service;

import com.healthcare.modules.appointment.entity.AppointmentEntity;
import com.healthcare.modules.appointment.service.AppointmentService;
import com.healthcare.modules.consultation.dto.ConsultationResponseDTO;
import com.healthcare.modules.consultation.dto.CreateConsultationDTO;
import com.healthcare.modules.consultation.dto.UpdateConsultationDTO;
import com.healthcare.modules.consultation.entity.ConsultationEntity;
import com.healthcare.modules.consultation.repository.ConsultationRepository;
import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.service.DoctorService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ConsultationRepository consultationRepository;

    public ConsultationServiceImpl(DoctorService doctorService, AppointmentService appointmentService, ConsultationRepository consultationRepository) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.consultationRepository = consultationRepository;
    }

    @Override
    public ConsultationResponseDTO createConsultation(CreateConsultationDTO createConsultationDTO) {

        if (consultationRepository.existsByAppointmentId(createConsultationDTO.appointmentId())) {
            throw new ApplicationException(ErrorMessage.CONSULTATION_ALREADY_EXISTS_FOR_APPOINTMENT, "");
        }

        DoctorEntity doctorEntity = this.doctorService.findDoctorEntityById(createConsultationDTO.createdByDoctor());
        AppointmentEntity appointmentEntity = this.appointmentService.findAppointmentEntityById(createConsultationDTO.appointmentId());

        ConsultationEntity newConsultation = new ConsultationEntity();
        newConsultation.setAppointment(appointmentEntity);
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

        return ConsultationResponseDTO.fromEntity(newConsultation);
    }

    @Override
    public ConsultationResponseDTO updateConsultation(UUID id, UpdateConsultationDTO updateConsultationDTO) {

        ConsultationEntity findConsultation = this.findConsultationEntityById(id);

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
}
