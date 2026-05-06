package com.healthcare.modules.appointment.service;

import com.healthcare.modules.appointment.dto.AppointmentResponseDTO;
import com.healthcare.modules.appointment.dto.CreateAppointmentDTO;
import com.healthcare.modules.appointment.dto.UpdateAppointmentDTO;
import com.healthcare.modules.appointment.entity.AppointmentEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(CreateAppointmentDTO createAppointmentDTO);
    AppointmentResponseDTO updateAppointment(UUID id, UpdateAppointmentDTO updateAppointmentDTO);
    PageResponse<AppointmentResponseDTO> findAllAppointments(int page, int size);
    AppointmentResponseDTO findAppointmentById(UUID id);
    AppointmentEntity findAppointmentEntityById(UUID id);
    void deleteAppointment(UUID id);

}
