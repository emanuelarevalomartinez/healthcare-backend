package com.healthcare.modules.appointment.service;

import com.healthcare.modules.appointment.dto.AppointmentResponseDTO;
import com.healthcare.modules.appointment.dto.CreateAppointmentDTO;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(CreateAppointmentDTO createAppointmentDTO);
}
