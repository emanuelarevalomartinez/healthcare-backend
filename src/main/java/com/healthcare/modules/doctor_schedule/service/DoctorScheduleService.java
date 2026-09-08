package com.healthcare.modules.doctor_schedule.service;

import com.healthcare.modules.doctor_schedule.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedule.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.entity.DoctorScheduleEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorScheduleService {
    List<DoctorScheduleResponseDTO> createDoctorSchedulesResponseDTO(
            CreateDoctorScheduleDTO createDoctorScheduleDTO);
    List<DoctorScheduleEntity> createDoctorSchedulesResponseEntities(
            CreateDoctorScheduleDTO createDoctorScheduleDTO);
    List<DoctorScheduleResponseDTO> updateDoctorSchedulesResponseDTO(
            UpdateDoctorScheduleDTO updateDoctorScheduleDTO);
    List<DoctorScheduleEntity> updateDoctorSchedulesResponseEntities(
            UpdateDoctorScheduleDTO updateDoctorScheduleDTO);
    PageResponse<DoctorScheduleResponseDTO> findAllDoctorSchedules(int page, int size);
    DoctorScheduleResponseDTO findDoctorScheduleById(UUID id);
    DoctorScheduleEntity findDoctorScheduleEntityById(UUID id);
    void deleteDoctorSchedule(UUID id);

}
