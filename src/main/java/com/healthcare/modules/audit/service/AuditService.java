package com.healthcare.modules.audit.service;

import com.healthcare.modules.audit.dto.AuditResponseDTO;
import com.healthcare.modules.audit.dto.CreateAuditDTO;
import com.healthcare.modules.audit.dto.UpdateAuditDTO;
import com.healthcare.modules.audit.entity.AuditEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface AuditService {
    AuditResponseDTO createAudit(CreateAuditDTO createAuditDTO);
    AuditResponseDTO updateAudit(UUID id, UpdateAuditDTO updateAuditDTO);
    PageResponse<AuditResponseDTO> findAllAudits(int page, int size);
    AuditResponseDTO findAuditById(UUID id);
    AuditEntity findAuditEntityById(UUID id);
    void deleteAudit(UUID id);
}
