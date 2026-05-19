package com.healthcare.modules.consultation.service;

import com.healthcare.modules.consultation.dto.ConsultationResponseDTO;
import com.healthcare.modules.consultation.dto.CreateConsultationDTO;
import com.healthcare.modules.consultation.dto.UpdateConsultationDTO;
import com.healthcare.modules.consultation.entity.ConsultationEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface ConsultationService {
    ConsultationResponseDTO createConsultation(CreateConsultationDTO createConsultationDTO);
    ConsultationResponseDTO updateConsultation(UUID id, UpdateConsultationDTO updateConsultationDTO);
    PageResponse<ConsultationResponseDTO> findAllConsultations(int page, int size);
    ConsultationResponseDTO findConsultationById(UUID id);
    ConsultationEntity findConsultationEntityById(UUID id);
    void deleteConsultation(UUID id);
}
