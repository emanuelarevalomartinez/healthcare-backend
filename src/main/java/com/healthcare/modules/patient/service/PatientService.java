package com.healthcare.modules.patient.service;

import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.dto.UpdatePatientDTO;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientService {
    PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO);
    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);
    List<PatientResponseDTO> findAllPatients();
    PatientResponseDTO findPatientById(UUID id);
   /* UserResponseDTO findUserByUsername(String username);
    UserResponseDTO findUserByEmail(String email);*/
    void deletePatient(UUID id);
    PatientEntity findPatientEntityById(UUID id);
}
