package com.healthcare.modules.consultation_histories.controller;

import com.healthcare.modules.consultation_histories.dto.ConsultationHistoriesResponseDTO;
import com.healthcare.modules.consultation_histories.dto.ConsultationHistoryGroupResponseDTO;
import com.healthcare.modules.consultation_histories.dto.CreateConsultationHistoriesDTO;
import com.healthcare.modules.consultation_histories.service.ConsultationHistoriesService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/consultation_histories")
public class ConsultationHistoriesController {

    private final ConsultationHistoriesService consultationHistoriesService;

    public ConsultationHistoriesController(ConsultationHistoriesService consultationHistoriesService) {
        this.consultationHistoriesService = consultationHistoriesService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ConsultationHistoriesResponseDTO>> createPatient(@Valid @RequestBody CreateConsultationHistoriesDTO createConsultationHistoriesDTO) {

        ConsultationHistoriesResponseDTO consultationHistoriesResponseDTO = consultationHistoriesService.createConsultationHistories(createConsultationHistoriesDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created consultation histories",
                consultationHistoriesResponseDTO
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ConsultationHistoryGroupResponseDTO>>> findConsultationHistoriesGroupedByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean ascending
    ) {

        PageResponse<ConsultationHistoryGroupResponseDTO> result = consultationHistoriesService.findConsultationHistoriesGroupedByDate(page, size, ascending);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                result
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteConsultationHistoryById(
            @PathVariable UUID id) {

        consultationHistoriesService.deleteConsultationHistoryById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully deleted consultation history",
                null
        );
    }

    @DeleteMapping("/by-date")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteConsultationHistoriesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        long deleted = consultationHistoriesService.deleteConsultationHistoriesByDate(date);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully deleted consultation histories by date",
                Map.of("deleted", deleted, "date", date.toString())
        );
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteAllConsultationHistories() {

        long deleted = consultationHistoriesService.deleteAllConsultationHistories();

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully deleted all consultation histories",
                Map.of("deleted", deleted)
        );
    }

}
