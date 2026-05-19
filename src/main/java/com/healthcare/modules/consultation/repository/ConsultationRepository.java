package com.healthcare.modules.consultation.repository;

import com.healthcare.modules.consultation.entity.ConsultationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {

    boolean existsByAppointmentId(UUID appointmentId);

    @Query("SELECT c FROM ConsultationEntity c")
    Page<ConsultationEntity> findAllConsultationsPaged(Pageable pageable);

}
