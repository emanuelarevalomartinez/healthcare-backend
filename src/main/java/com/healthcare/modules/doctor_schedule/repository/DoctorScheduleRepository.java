package com.healthcare.modules.doctor_schedule.repository;

import com.healthcare.modules.doctor_schedule.entity.DoctorScheduleEntity;
import com.healthcare.modules.doctor_schedule.enums.DoctorScheduleDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorScheduleEntity, UUID> {

    boolean existsByDoctorIdAndDayOfWeek(
            UUID doctorId,
            DoctorScheduleDay dayOfWeek
    );

    boolean existsByDoctorIdAndDayOfWeekAndIdNot(
            UUID doctorId,
            DoctorScheduleDay dayOfWeek,
            UUID id
    );

    Optional<DoctorScheduleEntity> findByDoctorIdAndDayOfWeek(
            UUID doctorId,
            DoctorScheduleDay dayOfWeek
    );

    @Query("SELECT ds FROM DoctorScheduleEntity ds")
    Page<DoctorScheduleEntity> findAllDoctorSchedulesPaged(Pageable pageable);

}
