package com.healthcare.modules.audits.service;

import com.healthcare.modules.audits.dto.AuditResponseDTO;
import com.healthcare.modules.audits.dto.CreateAuditDTO;
import com.healthcare.modules.audits.dto.UpdateAuditDTO;
import com.healthcare.modules.audits.entity.AuditEntity;
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
