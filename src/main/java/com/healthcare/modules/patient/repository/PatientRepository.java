package com.healthcare.modules.patient.repository;

import com.healthcare.modules.patient.entity.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientEntity, UUID>, JpaSpecificationExecutor<PatientEntity> {

    Optional<PatientEntity> findByMedicalRecordNumber(String medicalRecordNumber);
    Optional<PatientEntity> findByDocumentNumber(String documentNumber);

    @Query("SELECT p FROM PatientEntity p")
    Page<PatientEntity> findAllPatientsPaged(Pageable pageable);

}
