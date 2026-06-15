package com.healthcare.modules.audit.service;

import com.healthcare.modules.audit.dto.AuditResponseDTO;
import com.healthcare.modules.audit.dto.CreateAuditDTO;
import com.healthcare.modules.audit.dto.UpdateAuditDTO;
import com.healthcare.modules.audit.entity.AuditEntity;
import com.healthcare.modules.audit.repository.AuditRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditServiceImpl implements AuditService {

    private final UserService userService;
    private final AuditRepository auditRepository;

    public AuditServiceImpl(UserService userService, AuditRepository auditRepository) {
        this.userService = userService;
        this.auditRepository = auditRepository;
    }

    @Override
    public AuditResponseDTO createAudit(CreateAuditDTO createAuditDTO) {

        UserEntity userEntity = this.userService.findUserEntityById(createAuditDTO.userId());

        AuditEntity newAudit = new AuditEntity();
        newAudit.setUser(userEntity);
        newAudit.setAction(createAuditDTO.action());
        newAudit.setAffectedTable(createAuditDTO.affectedTable());
        newAudit.setRecordId(createAuditDTO.recordId());
        newAudit.setOldValue(createAuditDTO.oldValue());
        newAudit.setNewValue(createAuditDTO.newValue());
        newAudit.setTimestamp(LocalDateTime.now());

        this.auditRepository.save(newAudit);
        return AuditResponseDTO.fromEntity(newAudit);
    }

    @Override
    public AuditResponseDTO updateAudit(UUID id, UpdateAuditDTO updateAuditDTO) {

        AuditEntity findAudit = this.findAuditEntityById(id);

        if (updateAuditDTO.oldValue() != null) {
            findAudit.setOldValue(updateAuditDTO.oldValue());
        }

        if (updateAuditDTO.newValue() != null) {
            findAudit.setNewValue(updateAuditDTO.newValue());
        }

        AuditEntity auditUpdated = this.auditRepository.save(findAudit);
        return AuditResponseDTO.fromEntity(auditUpdated);
    }

    @Override
    public PageResponse<AuditResponseDTO> findAllAudits(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AuditEntity> result = auditRepository.findAllAuditsPaged(pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(AuditResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public AuditResponseDTO findAuditById(UUID id) {

        AuditEntity findAuditById = this.auditRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.AUDIT_NOT_FOUND_ID, "")
                );

        return AuditResponseDTO.fromEntity(findAuditById);
    }

    @Override
    public AuditEntity findAuditEntityById(UUID id) {
        return this.auditRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.AUDIT_NOT_FOUND_ID, id);
                });
    }

    @Override
    public void deleteAudit(UUID id) {
        AuditEntity audit = this.findAuditEntityById(id);
        auditRepository.deleteById(audit.getId());
    }
}
