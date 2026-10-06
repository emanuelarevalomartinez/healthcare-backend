package com.healthcare.modules.consultations.service;

import com.healthcare.modules.consultations.dto.ConsultationResponseDTO;
import com.healthcare.modules.consultations.dto.CreateConsultationDTO;
import com.healthcare.modules.consultations.dto.UpdateConsultationDTO;
import com.healthcare.modules.consultations.entity.ConsultationEntity;
import com.healthcare.shared.response.PageResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface ConsultationService {
    ConsultationResponseDTO createConsultation(CreateConsultationDTO createConsultationDTO);
    ConsultationResponseDTO updateConsultation(UUID id, UpdateConsultationDTO updateConsultationDTO);
    PageResponse<ConsultationResponseDTO> findAllConsultations(int page, int size);
    PageResponse<ConsultationResponseDTO> findConsultationsFiltered(int page, int size, boolean ascending, LocalDate date);
    PageResponse<ConsultationResponseDTO> searchConsultations(int page, int size, Boolean ascending, String search);
    ConsultationResponseDTO findConsultationById(UUID id);
    ConsultationEntity findConsultationEntityById(UUID id);
    void deleteConsultation(UUID id);
}
