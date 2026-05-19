package com.healthcare.modules.reminder.entity;

import com.healthcare.modules.reminder.enums.ReminderType;
import com.healthcare.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "reminder",
        indexes = {
                @Index(name = "idx_reminder_user", columnList = "user_id"),
                @Index(name = "idx_reminder_reminder_date", columnList = "reminder_date"),
                @Index(name = "idx_reminder_read", columnList = "is_read"),
                @Index(name = "idx_reminder_user_read", columnList = "user_id, is_read")
        }
)

public class ReminderEntity {

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
            nullable = false
    )
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type",
            nullable = false,
            length = 30
    )
    private ReminderType type;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(
            name = "reminder_date",
            nullable = false
    )
    private LocalDateTime reminderDate;

    @Column(
            name = "is_read",
            nullable = false
    )
    private Boolean isRead = false;

    @Column(
            name = "creation_date",
            nullable = false,
            updatable = false
    )
    private LocalDateTime creationDate;

    public ReminderEntity() {
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

    public ReminderType getType() {
        return type;
    }

    public void setType(ReminderType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
