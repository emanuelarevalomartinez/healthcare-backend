package com.healthcare.modules.patient.service;

import com.healthcare.modules.patient.enums.DocumentType;
import com.healthcare.modules.patient.enums.Sex;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.dto.UpdatePatientDTO;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.patient.repository.PatientRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserService userService;

    public PatientServiceImpl(PatientRepository patientRepository, UserService userService) {
        this.patientRepository = patientRepository;
        this.userService = userService;
    }

    @Override
    public PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO) {

        try{

            if (patientRepository.findByMedicalRecordNumber(createPatientDTO.medicalRecordNumber()).isPresent()) {
                throw new ApplicationException(ErrorMessage.PATIENT_MRN_CONFLICT, createPatientDTO.medicalRecordNumber());
            }

            if (patientRepository.findByDocumentNumber(createPatientDTO.documentNumber()).isPresent()) {
                throw new ApplicationException(ErrorMessage.PATIENT_DOCUMENT_CONFLICT,  createPatientDTO.documentNumber());
            }

            UserEntity user = userService.findUserEntityById(createPatientDTO.createdById());

            PatientEntity newPatient = new PatientEntity();
            newPatient.setMedicalRecordNumber(createPatientDTO.medicalRecordNumber());
            newPatient.setFullName(createPatientDTO.fullName());
            newPatient.setDocumentType(createPatientDTO.documentType());
            newPatient.setDocumentNumber(createPatientDTO.documentNumber());
            newPatient.setBirthDate(createPatientDTO.birthDate());
            newPatient.setSex(createPatientDTO.sex());
            newPatient.setPhone(createPatientDTO.phone());
            newPatient.setEmail(createPatientDTO.email());
            newPatient.setAddress(createPatientDTO.address());
            newPatient.setNotes(createPatientDTO.notes());
            newPatient.setCreatedBy(user);

            PatientEntity saved = this.patientRepository.save(newPatient);
            return PatientResponseDTO.fromEntity(saved);

        }  catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        try {

            PatientEntity findPatient = this.findPatientEntityById(id);

            if (updatePatientDTO.medicalRecordNumber() != null) {
                findPatient.setMedicalRecordNumber(updatePatientDTO.medicalRecordNumber());
            }

            if (updatePatientDTO.fullName() != null) {
                findPatient.setFullName(updatePatientDTO.fullName());
            }

            if (updatePatientDTO.documentType() != null) {
                findPatient.setDocumentType(updatePatientDTO.documentType());
            }

            if (updatePatientDTO.documentNumber() != null) {
                findPatient.setDocumentNumber(updatePatientDTO.documentNumber());
            }

            if (updatePatientDTO.birthDate() != null) {
                findPatient.setBirthDate(updatePatientDTO.birthDate());
            }

            if (updatePatientDTO.sex() != null) {
                findPatient.setSex(updatePatientDTO.sex());
            }

            if (updatePatientDTO.phone() != null) {
                findPatient.setPhone(updatePatientDTO.phone());
            }

            if (updatePatientDTO.email() != null) {
                findPatient.setEmail(updatePatientDTO.email());
            }

            if (updatePatientDTO.address() != null) {
                findPatient.setAddress(updatePatientDTO.address());
            }

            if (updatePatientDTO.notes() != null) {
                findPatient.setNotes(updatePatientDTO.notes());
            }

            PatientEntity patientUpdated = this.patientRepository.save(findPatient);

            return PatientResponseDTO.fromEntity(patientUpdated);

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex){
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PageResponse<PatientResponseDTO> findAllPatients(int page, int size) {

        try{

            Pageable pageable = PageRequest.of(page, size);
            Page<PatientEntity> result = this.patientRepository.findAllPatientsPaged(pageable);

            return new PageResponse<>(
                    result.getContent()
                            .stream()
                            .map(PatientResponseDTO::fromEntity)
                            .toList(),
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages()
            );

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PatientResponseDTO findPatientById(UUID id) {
        try{

            PatientEntity findPatientById = this.patientRepository.findById(id)
                    .orElseThrow( () -> new ApplicationException(ErrorMessage.PATIENT_NOT_FOUND_ID, id)
                    );

            return PatientResponseDTO.fromEntity(findPatientById);

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void deletePatient(UUID id) {

        try{

            PatientEntity user = this.findPatientEntityById(id);
            patientRepository.deleteById(user.getId());

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PatientEntity findPatientEntityById(UUID id) {

        return this.patientRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.PATIENT_NOT_FOUND_ID, id);
                });
    }

    @Override
    public PageResponse<PatientResponseDTO> findPatientsByArguments(String search, Sex sex, DocumentType documentType, int page, int size) {

        return null;
    }


}
