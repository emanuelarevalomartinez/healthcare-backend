package com.healthcare.modules.doctor_schedules.service;

import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctor_schedules.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedules.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedules.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedules.entity.DoctorScheduleEntity;
import com.healthcare.modules.doctor_schedules.enums.DoctorScheduleDay;
import com.healthcare.shared.response.PageResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorScheduleService {
    List<DoctorScheduleResponseDTO> createDoctorSchedulesResponseDTO(
            CreateDoctorScheduleDTO createDoctorScheduleDTO);
    List<DoctorScheduleEntity> createDoctorSchedulesResponseEntities(
            CreateDoctorScheduleDTO createDoctorScheduleDTO);
    List<DoctorScheduleEntity> createDoctorSchedulesForDoctor(
            DoctorEntity doctor,
            List<CreateDoctorScheduleDTO.DayScheduleDTO> schedules
    );
    List<DoctorScheduleResponseDTO> updateDoctorSchedulesResponseDTO(
            UpdateDoctorScheduleDTO updateDoctorScheduleDTO);
    List<DoctorScheduleEntity> updateDoctorSchedulesResponseEntities(
            UpdateDoctorScheduleDTO updateDoctorScheduleDTO);
    PageResponse<DoctorScheduleResponseDTO> findAllDoctorSchedules(int page, int size);
    DoctorScheduleResponseDTO findDoctorScheduleById(UUID id);
    DoctorScheduleEntity findDoctorScheduleEntityById(UUID id);
    DoctorScheduleEntity findDoctorScheduleByDoctorAndDayOfWeek(UUID doctorId, DoctorScheduleDay day);
    List<DoctorScheduleEntity> findByDoctorId(UUID doctorId);
    void deleteAllByIds(List<UUID> ids);
    void deleteDoctorSchedule(UUID id);

}
