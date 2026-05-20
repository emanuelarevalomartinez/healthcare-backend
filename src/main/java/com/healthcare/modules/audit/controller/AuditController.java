package com.healthcare.modules.audit.controller;

import com.healthcare.modules.audit.dto.AuditResponseDTO;
import com.healthcare.modules.audit.dto.CreateAuditDTO;
import com.healthcare.modules.audit.dto.UpdateAuditDTO;
import com.healthcare.modules.audit.service.AuditService;
import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.doctor.dto.UpdateDoctorDTO;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/audits")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuditResponseDTO>> createAudit(@Valid @RequestBody CreateAuditDTO createAuditDTO) {

        AuditResponseDTO audit = auditService.createAudit(createAuditDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created audit",
                audit
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AuditResponseDTO>> findAuditById(@PathVariable UUID id) {

        AuditResponseDTO audit = this.auditService.findAuditById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                audit
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AuditResponseDTO>>> findAllAudits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<AuditResponseDTO> audits = auditService.findAllAudits(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                audits
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AuditResponseDTO>> updateAudit(@PathVariable UUID id , @Valid @RequestBody UpdateAuditDTO updateAuditDTO) {

        AuditResponseDTO auditUpdate = this.auditService.updateAudit(id, updateAuditDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Audit updated successfully",
                auditUpdate
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteAuditById(@PathVariable UUID id) {

        this.auditService.deleteAudit(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete audit",
                null
        );
    }

}
