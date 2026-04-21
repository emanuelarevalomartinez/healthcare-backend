package com.healthcare.modules.patient.service;

import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.dto.UpdatePatientDTO;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.modules.patient.enums.Sex;
import com.healthcare.shared.response.PageResponse;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientService {
    PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO);
    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);
    PageResponse<PatientResponseDTO> findAllPatients(int page, int size);
    PatientResponseDTO findPatientById(UUID id);
   /* UserResponseDTO findUserByUsername(String username);
    UserResponseDTO findUserByEmail(String email);*/
    void deletePatient(UUID id);
    PatientEntity findPatientEntityById(UUID id);
    PageResponse<PatientResponseDTO> findPatientsByArguments(String search, Sex sex, DocumentType documentType, int page, int size);
}
