package com.healthcare.modules.audit.dto;

import com.healthcare.modules.audit.entity.AuditEntity;
import com.healthcare.modules.audit.enums.AuditAction;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditResponseDTO(
        UUID id,
        AuditAction action,
        String affectedTable,
        String recordId,
        String oldValue,
        String newValue,
        LocalDateTime timestamp

) {

    public static AuditResponseDTO fromEntity(AuditEntity audit) {

        return new AuditResponseDTO(
                audit.getId(),
                audit.getAction(),
                audit.getAffectedTable(),
                audit.getRecordId(),
                audit.getOldValue(),
                audit.getNewValue(),
                audit.getTimestamp()
        );
    }

}
