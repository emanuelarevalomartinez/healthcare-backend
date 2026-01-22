package com.healthcare.modules.patient.service;

import com.healthcare.exceptions.ApplicationException;
import com.healthcare.modules.patient.dto.CreatePatientDTO;
import com.healthcare.modules.patient.dto.PatientResponseDTO;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.patient.repository.PatientRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import org.springframework.stereotype.Service;

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

}
