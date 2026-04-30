package com.healthcare.modules.doctor.controller;


import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResposeDTO;
import com.healthcare.modules.doctor.dto.UpdateDoctorDTO;
import com.healthcare.modules.doctor.service.DoctorService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<DoctorResposeDTO>> findDoctorById(@PathVariable UUID id) {

        DoctorResposeDTO doctor = this.doctorService.findDoctorById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                doctor
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<DoctorResposeDTO>> updateUser(@PathVariable UUID id , @Valid @RequestBody UpdateDoctorDTO updateDoctorDTO) {

        DoctorResposeDTO doctorUpdate = this.doctorService.updateDoctor(id, updateDoctorDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Doctor updated successfully",
                doctorUpdate
        );
    }

}
