package com.healthcare.modules.doctor.controller;


import com.healthcare.modules.doctor.dto.*;
import com.healthcare.modules.doctor.service.DoctorService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
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
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> createDoctor(@Valid @RequestBody CreateDoctorDTO createDoctorDTO) {

        DoctorResponseDTO doctor = doctorService.createDoctor(createDoctorDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created doctor",
                doctor
        );
    }

    @PostMapping("/create-with-user")
    public ResponseEntity<ApiResponse<DoctorResponseWithUserDTO>> createDoctorWithUser(@Valid @RequestBody CreateDoctorWithUserDTO createDoctorWithUserDTO) {

        DoctorResponseWithUserDTO doctorWithUser = doctorService.createDoctorWithUser(createDoctorWithUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created doctor with user",
                doctorWithUser
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> findDoctorById(@PathVariable UUID id) {

        DoctorResponseDTO doctor = this.doctorService.findDoctorById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                doctor
        );
    }

    @GetMapping
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
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> updateDoctor(@PathVariable UUID id, @Valid @RequestBody UpdateDoctorDTO updateDoctorDTO) {

        DoctorResponseDTO doctorUpdate = this.doctorService.updateDoctor(id, updateDoctorDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Doctor updated successfully",
                doctorUpdate
        );
    }

    @PutMapping("/update-with-user/{userId}")
    public ResponseEntity<ApiResponse<DoctorResponseWithUserDTO>> updateDoctorWithUser(@PathVariable UUID userId, @Valid @RequestBody UpdateDoctorWithUserDTO updateDoctorWithUserDTO) {

        DoctorResponseWithUserDTO doctorWithUserUpdate = doctorService.updateDoctorWithUser(userId, updateDoctorWithUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Doctor with user updated successfully",
                doctorWithUserUpdate
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteDoctorById(@PathVariable UUID id) {

        this.doctorService.deleteDoctor(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete doctor",
                null
        );
    }

    @DeleteMapping("/delete-with-user/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteDoctorByUserId(@PathVariable UUID userId) {

        this.doctorService.deleteDoctorByUserId(userId);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete doctor by user",
                null
        );
    }

}
