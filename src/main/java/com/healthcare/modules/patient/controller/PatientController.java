package com.healthcare.modules.patient.controller;

import com.healthcare.config.response.ApiResponse;
import com.healthcare.config.response.ResponseHandler;
import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient(@Valid @RequestBody CreatePatientDTO createPatientDTO) {

        PatientResponseDTO patient = patientService.createPatient(createPatientDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created patient",
                patient
        );
    }

}
