package com.healthcare.modules.audit.entity;

import com.healthcare.modules.audit.enums.AuditAction;
import com.healthcare.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "audit",
        indexes = {
                // Simple indexes
                @Index(name = "idx_audit_user_id", columnList = "user_id"),
                @Index(name = "idx_audit_action", columnList = "action"),
                @Index(name = "idx_audit_timestamp", columnList = "timestamp"),
                @Index(name = "idx_audit_affected_table", columnList = "affected_table"),
                @Index(name = "idx_audit_record_id", columnList = "record_id"),
                @Index(name = "idx_audit_ip_address", columnList = "ip_address"),
                // Composite indexes for frequent queries
                @Index(name = "idx_audit_timestamp_user", columnList = "timestamp, user_id"),
                @Index(name = "idx_audit_timestamp_action", columnList = "timestamp, action"),
                @Index(name = "idx_audit_user_action", columnList = "user_id, action"),
                @Index(name = "idx_audit_table_record", columnList = "affected_table, record_id"),
                @Index(name = "idx_audit_user_timestamp_action", columnList = "user_id, timestamp, action"),
                // Index for date range queries
                @Index(name = "idx_audit_timestamp_desc", columnList = "timestamp DESC"),
                // Indexes for specific entity auditing
                @Index(name = "idx_audit_patient_audit", columnList = "affected_table, record_id, action"),
                @Index(name = "idx_audit_appointment_audit", columnList = "affected_table, record_id, action"),
                // Index for IP searches
                @Index(name = "idx_audit_ip_timestamp", columnList = "ip_address, timestamp")
        }
)
public class AuditEntity {

    @Id
    @UuidGenerator
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_audit_user")
    )
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 50)
    private AuditAction action;

    @Column(name = "affected_table", length = 200)
    private String affectedTable;

    @Column(name = "record_id", length = 100)
    private String recordId;

    @Column(name = "old_value", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private String newValue;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // Constructors
    public AuditEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getAffectedTable() {
        return affectedTable;
    }

    public void setAffectedTable(String affectedTable) {
        this.affectedTable = affectedTable;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
