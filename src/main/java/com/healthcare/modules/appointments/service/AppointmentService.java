package com.healthcare.modules.appointments.service;

import com.healthcare.modules.appointments.dto.*;
import com.healthcare.modules.appointments.entity.AppointmentEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(CreateAppointmentDTO createAppointmentDTO);

    AppointmentResponseDTO updateAppointment(UUID id, UpdateAppointmentDTO updateAppointmentDTO);

    PageResponse<AppointmentResponseDTO> findAllAppointments(int page, int size);

    AppointmentResponseDTO findAppointmentById(UUID id);

    PageResponse<AppointmentResponseDTO> findAppointmentsFiltered(AppointmentFilterParams appointmentFilterParams);

    PageResponse<AppointmentResponseDTO> searchAppointments(AppointmentSearchParams params);

    AppointmentEntity findAppointmentEntityById(UUID id);

    void deleteAppointment(UUID id);

    void markAsAttended(UUID appointmentId);

}
