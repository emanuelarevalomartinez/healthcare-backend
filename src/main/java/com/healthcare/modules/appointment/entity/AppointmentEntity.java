package com.healthcare.modules.appointment.entity;


import com.healthcare.modules.appointment.enums.AppointmentStatus;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.patient.entity.PatientEntity;
import com.healthcare.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "appointment",
        indexes = {
                @Index(
                        name = "idx_appointment_patient",
                        columnList = "patient_id"
                ),
                @Index(
                        name = "idx_appointment_doctor",
                        columnList = "doctor_id"
                ),
                @Index(
                        name = "idx_appointment_date_time",
                        columnList = "appointment_date_time"
                ),
                @Index(
                        name = "idx_appointment_status",
                        columnList = "status"
                )
        }
)
public class AppointmentEntity {

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
            name = "patient_id",
            nullable = false
    )
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "doctor_id",
            nullable = false
    )
    private DoctorEntity doctor;

    @Column(
            name = "appointment_date_time",
            nullable = false
    )
    private LocalDateTime appointmentDateTime;

    @Column(
            name = "duration_minutes",
            nullable = false
    )
    private Integer durationMinutes;

    @Column(
            name = "consultation_reason",
            nullable = false,
            length = 255
    )
    private String consultationReason;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "cancelled_by"
    )
    private UserEntity cancelledBy;

    @Column(
            name = "cancellation_reason",
            length = 255
    )
    private String cancellationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            nullable = false
    )
    private UserEntity createdBy;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "attended_at")
    private LocalDateTime attendedAt;

    @Column(
            name = "notes",
            length = 500
    )
    private String notes;

    public AppointmentEntity() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getConsultationReason() {
        return consultationReason;
    }

    public void setConsultationReason(String consultationReason) {
        this.consultationReason = consultationReason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public UserEntity getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(UserEntity cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDateTime getAttendedAt() {
        return attendedAt;
    }

    public void setAttendedAt(LocalDateTime attendedAt) {
        this.attendedAt = attendedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
