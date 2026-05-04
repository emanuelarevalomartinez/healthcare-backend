package com.healthcare.modules.appointment.controller;


import com.healthcare.modules.appointment.dto.AppointmentResponseDTO;
import com.healthcare.modules.appointment.dto.CreateAppointmentDTO;
import com.healthcare.modules.appointment.service.AppointmentService;
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
@RequestMapping("/appointments")
public class AppointmentController {

    // TODO
    // generar el endpoint de cqancelar la cita

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> createAppointment(@Valid @RequestBody CreateAppointmentDTO createAppointmentDTO) {

        AppointmentResponseDTO appointment = appointmentService.createAppointment(createAppointmentDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created appointment",
                appointment
        );
    }
}
