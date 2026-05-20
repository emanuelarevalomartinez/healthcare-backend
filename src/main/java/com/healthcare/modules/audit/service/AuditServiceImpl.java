package com.healthcare.modules.audit.service;

import com.healthcare.modules.audit.dto.AuditResponseDTO;
import com.healthcare.modules.audit.dto.CreateAuditDTO;
import com.healthcare.modules.audit.dto.UpdateAuditDTO;
import com.healthcare.modules.audit.entity.AuditEntity;
import com.healthcare.shared.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditServiceImpl implements AuditService {

    @Override
    public AuditResponseDTO createAudit(CreateAuditDTO createAuditDTO) {
        return null;
    }

    @Override
    public AuditResponseDTO updateAudit(UUID id, UpdateAuditDTO updateAuditDTO) {
        return null;
    }

    @Override
    public PageResponse<AuditResponseDTO> findAllAudits(int page, int size) {
        return null;
    }

    @Override
    public AuditResponseDTO findAuditById(UUID id) {
        return null;
    }

    @Override
    public AuditEntity findAuditEntityById(UUID id) {
        return null;
    }

    @Override
    public void deleteAudit(UUID id) {

    }
}
