package com.healthcare.modules.doctor.service;

import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResposeDTO;

public interface DoctorService {
    DoctorResposeDTO createDoctor(CreateDoctorDTO createDoctorDTO);
}
