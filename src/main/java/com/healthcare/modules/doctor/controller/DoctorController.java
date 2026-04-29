package com.healthcare.modules.doctor.controller;


import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResposeDTO;
import com.healthcare.modules.doctor.service.DoctorService;
import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.service.PatientService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResposeDTO>> createPatient(@Valid @RequestBody CreateDoctorDTO createDoctorDTO) {

        DoctorResposeDTO doctor = doctorService.createDoctor(createDoctorDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created doctor",
                doctor
        );
    }

}
