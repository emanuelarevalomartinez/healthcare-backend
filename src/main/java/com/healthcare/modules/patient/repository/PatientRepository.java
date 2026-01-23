package com.healthcare.modules.patient.repository;

import com.healthcare.modules.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {

    Optional<PatientEntity> findByMedicalRecordNumber(String medicalRecordNumber);
    Optional<PatientEntity> findByDocumentNumber(String documentNumber);

}
