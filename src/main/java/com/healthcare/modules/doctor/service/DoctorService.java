package com.healthcare.modules.doctor.service;

import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResposeDTO;
import com.healthcare.modules.doctor.dto.UpdateDoctorDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;

import java.util.UUID;

public interface DoctorService {
    DoctorResposeDTO createDoctor(CreateDoctorDTO createDoctorDTO);
    DoctorResposeDTO updateDoctor(UUID id, UpdateDoctorDTO updateDoctorDTO);
    DoctorResposeDTO findDoctorById(UUID id);
    DoctorEntity findDoctorEntityById(UUID id);
}
