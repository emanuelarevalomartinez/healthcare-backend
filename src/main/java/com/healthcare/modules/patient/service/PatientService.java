package com.healthcare.modules.patient.service;

import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientService {
    PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO);

}
