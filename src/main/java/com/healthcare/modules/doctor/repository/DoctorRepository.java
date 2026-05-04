package com.healthcare.modules.doctor.repository;

import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DoctorEntity d WHERE d.user.id = :userId")
    boolean existsByUserId(@Param("userId") UUID userId);

    @Query("SELECT d FROM DoctorEntity d")
    Page<DoctorEntity> findAllDoctorsPaged(Pageable pageable);

}
