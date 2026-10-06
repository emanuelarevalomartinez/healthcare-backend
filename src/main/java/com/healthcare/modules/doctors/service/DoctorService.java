package com.healthcare.modules.doctors.service;

import com.healthcare.modules.doctors.dto.*;
import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface DoctorService {
    DoctorWithSchedulesResponseDTO createDoctorWithSchedules(CreateDoctorWithSchedulesDTO createDoctorWithSchedulesDTO);
    DoctorResponseDTO updateDoctor(UUID id, UpdateDoctorDTO updateDoctorDTO);
    DoctorWithUserAndScheduleResponseDTO createDoctorWithUserAndSchedule(CreateDoctorWithUserAndScheduleDTO createDoctorWithUserAndScheduleDTO);
    DoctorWithUserAndScheduleResponseDTO updateDoctorWithUserAndSchedule(UUID userId, UpdateDoctorWithUserAndScheduleDTO updateDoctorWithUserDTO);
    PageResponse<DoctorResponseDTO> findAllDoctors(int page, int size);
    DoctorResponseDTO findDoctorById(UUID id);
    PageResponse<DoctorWithUserAndScheduleResponseDTO> findDoctorsFiltered(int page, int size, String search);
    DoctorEntity findDoctorEntityById(UUID id);
    void deleteDoctor(UUID id);
    void deleteDoctorAndItScheduleByUserId(UUID userId);

}
