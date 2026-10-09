package com.healthcare.modules.consultation_histories.dto;

import java.time.LocalDate;
import java.util.List;

public record ConsultationHistoryGroupResponseDTO(
        LocalDate date,
        List<ConsultationHistoriesResponseDTO> histories
) {
}
