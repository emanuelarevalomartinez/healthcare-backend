package com.healthcare.modules.patient.controller;

import com.healthcare.modules.patient.dto.UpdatePatientDTO;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient(@Valid @RequestBody CreatePatientDTO createPatientDTO) {

        PatientResponseDTO patient = patientService.createPatient(createPatientDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created patient",
                patient
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> findAllPatients() {

        List<PatientResponseDTO> patients = patientService.findAllPatients();

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                patients
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> findPatientById(@PathVariable UUID id) {

        PatientResponseDTO patient = patientService.findPatientById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                patient
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(@PathVariable UUID id, @Valid @RequestBody UpdatePatientDTO updatePatientDTO) {

        PatientResponseDTO updatedPatient = patientService.updatePatient(id, updatePatientDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Patient updated successfully",
                updatedPatient
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deletePatientById(@PathVariable UUID id) {

        this.patientService.deletePatient(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete patient",
                null
        );
    }

}
