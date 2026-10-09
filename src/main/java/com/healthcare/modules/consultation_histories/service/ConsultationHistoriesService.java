package com.healthcare.modules.consultation_histories.service;

import com.healthcare.modules.consultation_histories.dto.ConsultationHistoriesResponseDTO;
import com.healthcare.modules.consultation_histories.dto.ConsultationHistoryGroupResponseDTO;
import com.healthcare.modules.consultation_histories.dto.CreateConsultationHistoriesDTO;
import com.healthcare.shared.response.PageResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface ConsultationHistoriesService {
    ConsultationHistoriesResponseDTO createConsultationHistories(CreateConsultationHistoriesDTO createConsultationHistoriesDTO);
    PageResponse<ConsultationHistoryGroupResponseDTO> findConsultationHistoriesGroupedByDate(int page, int size, boolean ascending);
    void deleteConsultationHistoryById(UUID id);
    long deleteConsultationHistoriesByDate(LocalDate date);
    long deleteAllConsultationHistories();
}
