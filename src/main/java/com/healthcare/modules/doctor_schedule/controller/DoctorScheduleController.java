package com.healthcare.modules.doctor_schedule.controller;

import com.healthcare.modules.doctor.dto.*;
import com.healthcare.modules.doctor_schedule.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedule.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.service.DoctorScheduleService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor_schedule")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
        this.doctorScheduleService = doctorScheduleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDTO>>> createDoctorSchedules(@Valid @RequestBody CreateDoctorScheduleDTO createDoctorScheduleDTO) {

        List<DoctorScheduleResponseDTO> doctorSchedules = doctorScheduleService.createDoctorSchedulesResponseDTO(createDoctorScheduleDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created doctor schedule",
                doctorSchedules
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDTO>> findDoctorScheduleById(@PathVariable UUID id) {

        DoctorScheduleResponseDTO doctorSchedule = this.doctorScheduleService.findDoctorScheduleById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                doctorSchedule
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DoctorScheduleResponseDTO>>> findAllDoctorSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<DoctorScheduleResponseDTO> doctorSchedules = doctorScheduleService.findAllDoctorSchedules(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                doctorSchedules
        );
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponseDTO>>> updateDoctorSchedule(@Valid @RequestBody UpdateDoctorScheduleDTO updateDoctorScheduleDTO) {

        List<DoctorScheduleResponseDTO> doctorSchedulesUpdate = this.doctorScheduleService.updateDoctorSchedulesResponseDTO(updateDoctorScheduleDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Doctor schedule updated successfully",
                doctorSchedulesUpdate
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteDoctorScheduleById(@PathVariable UUID id) {

        this.doctorScheduleService.deleteDoctorSchedule(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete doctor schedule",
                null
        );
    }

}
