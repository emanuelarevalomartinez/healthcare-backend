package com.healthcare.modules.audit.repository;

import com.healthcare.modules.audit.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, UUID> {
}
