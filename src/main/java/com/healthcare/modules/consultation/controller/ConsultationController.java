package com.healthcare.modules.consultation.controller;


import com.healthcare.modules.appointment.dto.AppointmentResponseDTO;
import com.healthcare.modules.appointment.dto.AppointmentSearchParams;
import com.healthcare.modules.appointment.enums.AppointmentStatus;
import com.healthcare.modules.consultation.dto.ConsultationResponseDTO;
import com.healthcare.modules.consultation.dto.CreateConsultationDTO;
import com.healthcare.modules.consultation.dto.UpdateConsultationDTO;
import com.healthcare.modules.consultation.service.ConsultationService;
import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultationResponseDTO>> createConsultation(@Valid @RequestBody CreateConsultationDTO createConsultationDTO) {

        ConsultationResponseDTO consultation = consultationService.createConsultation(createConsultationDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created consultation",
                consultation
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ConsultationResponseDTO>> findConsultationById(@PathVariable UUID id) {

        ConsultationResponseDTO consultation = this.consultationService.findConsultationById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                consultation
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ConsultationResponseDTO>>> findAllConsultations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<ConsultationResponseDTO> consultations = consultationService.findAllConsultations(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                consultations
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PageResponse<ConsultationResponseDTO>>> findConsultationsFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {

        PageResponse<ConsultationResponseDTO> consultations = this.consultationService.findConsultationsFiltered(page, size, ascending, date);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                consultations
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ConsultationResponseDTO>>> searchConsultations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = true) String searchTerm
    ) {

        PageResponse<ConsultationResponseDTO> consultations = consultationService.searchConsultations(page, size, ascending, searchTerm);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                consultations
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ConsultationResponseDTO>> updateConsultation(@PathVariable UUID id , @Valid @RequestBody UpdateConsultationDTO updateConsultationDTO) {

        ConsultationResponseDTO consultationUpdate = this.consultationService.updateConsultation(id, updateConsultationDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Consultation updated successfully",
                consultationUpdate
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteConsultationById(@PathVariable UUID id) {

        this.consultationService.deleteConsultation(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete consultation",
                null
        );
    }

}
