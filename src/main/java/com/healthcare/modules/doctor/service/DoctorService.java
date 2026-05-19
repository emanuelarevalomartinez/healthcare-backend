package com.healthcare.modules.doctor.service;

import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.doctor.dto.UpdateDoctorDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface DoctorService {
    DoctorResponseDTO createDoctor(CreateDoctorDTO createDoctorDTO);
    DoctorResponseDTO updateDoctor(UUID id, UpdateDoctorDTO updateDoctorDTO);
    PageResponse<DoctorResponseDTO> findAllDoctors(int page, int size);
    DoctorResponseDTO findDoctorById(UUID id);
    DoctorEntity findDoctorEntityById(UUID id);
    void deleteDoctor(UUID id);

}
