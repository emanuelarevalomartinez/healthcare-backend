package com.healthcare.modules.appointments.repository;

import com.healthcare.modules.appointments.entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID>, JpaSpecificationExecutor<AppointmentEntity> {

    @Query("SELECT a FROM AppointmentEntity a")
    Page<AppointmentEntity> findAllAppointmentsPaged(Pageable pageable);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM appointment a
                WHERE a.doctor_id = :doctorId
                  AND a.status <> 'CANCELLED'
                  AND a.appointment_date_time < :newEnd
                  AND :newStart < a.appointment_date_time + (a.duration_minutes * INTERVAL '1 minute'))
            """, nativeQuery = true)
    boolean existsDoctorConflict(
            @Param("doctorId") UUID doctorId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );

    @Query(value = """
     SELECT EXISTS (
       SELECT 1
       FROM appointment a
       where a.patient_id = :patientId
       AND a.status <> 'CANCELLED'
       AND a.appointment_date_time < :newEnd
       AND :newStart < a.appointment_date_time + (a.duration_minutes * INTERVAL '1 minute')
     )
""", nativeQuery = true)
    boolean existsPatientConflict(
            @Param("patientId") UUID patientId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );

}
