package com.healthcare.modules.consultation.controller;


import com.healthcare.modules.consultation.dto.ConsultationResponseDTO;
import com.healthcare.modules.consultation.dto.CreateConsultationDTO;
import com.healthcare.modules.consultation.service.ConsultationService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultationResponseDTO>> createPatient(@Valid @RequestBody CreateConsultationDTO createConsultationDTO) {

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

 /*   @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DoctorResponseDTO>>> findAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<DoctorResponseDTO> doctors = doctorService.findAllDoctors(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                doctors
        );
    }*/

 /*   @PutMapping("{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> updateDoctor(@PathVariable UUID id , @Valid @RequestBody UpdateDoctorDTO updateDoctorDTO) {

        DoctorResponseDTO doctorUpdate = this.doctorService.updateDoctor(id, updateDoctorDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Doctor updated successfully",
                doctorUpdate
        );
    }*/

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
