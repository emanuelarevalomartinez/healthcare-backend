package com.healthcare.modules.patient.controller;

import com.healthcare.modules.patient.dto.UpdatePatientDTO;
import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.modules.patient.enums.Sex;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<PageResponse<PatientResponseDTO>>> findAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<PatientResponseDTO> patients = patientService.findAllPatients(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                patients
        );
    }

    @GetMapping("filter")
    public ResponseEntity<ApiResponse<PageResponse<PatientResponseDTO>>> findPatientsByArguments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) DocumentType documentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<PatientResponseDTO> patientsFiltered = patientService.findPatientsByArguments(search, sex, documentType, page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                patientsFiltered
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
