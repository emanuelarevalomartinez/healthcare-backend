package com.healthcare.modules.audit.repository;

import com.healthcare.modules.audit.entity.AuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, UUID> {

    @Query("SELECT a FROM AuditEntity a")
    Page<AuditEntity> findAllAuditsPaged(Pageable pageable);

}
