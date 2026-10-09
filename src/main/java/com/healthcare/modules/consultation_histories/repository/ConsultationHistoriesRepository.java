package com.healthcare.modules.consultation_histories.repository;

import com.healthcare.modules.consultation_histories.entity.ConsultationHistoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ConsultationHistoriesRepository extends JpaRepository<ConsultationHistoriesEntity, UUID>, JpaSpecificationExecutor<ConsultationHistoriesEntity> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ConsultationHistoriesEntity h WHERE h.registeredAt BETWEEN :start AND :end")
    long deleteByRegisteredAtBetween(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

}
