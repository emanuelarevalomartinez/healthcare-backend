package com.healthcare.modules.consultation_histories.entity;

import com.healthcare.modules.consultation_histories.enums.ConsultationHistoriesEventType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "consultation_histories"
)
public class ConsultationHistoriesEntity {

    @Id
    @UuidGenerator
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @Column(name = "consultation_id", updatable = false)
    private UUID consultationId;

    @Column(name = "appointment_id", updatable = false)
    private UUID appointmentId;

    @Column(name = "patient_id", updatable = false)
    private UUID patientId;

    @Column(name = "doctor_id", updatable = false)
    private UUID doctorId;

    @Column(name = "performed_by_user_id", updatable = false)
    private UUID performedByUserId;

    @Column(name = "consultation_name", length = 150, updatable = false)
    private String consultationName;

    @Column(name = "appointment_name", length = 150, updatable = false)
    private String appointmentName;

    @Column(name = "patient_name", length = 150, updatable = false)
    private String patientName;

    @Column(name = "doctor_name", length = 150, updatable = false)
    private String doctorName;

    @Column(name = "performed_by_user_name", length = 150, updatable = false)
    private String performedByUserName;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40, updatable = false)
    private ConsultationHistoriesEventType eventType;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    public ConsultationHistoriesEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(UUID consultationId) {
        this.consultationId = consultationId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getPerformedByUserId() {
        return performedByUserId;
    }

    public void setPerformedByUserId(UUID performedByUserId) {
        this.performedByUserId = performedByUserId;
    }

    public String getConsultationName() {
        return consultationName;
    }

    public void setConsultationName(String consultationName) {
        this.consultationName = consultationName;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPerformedByUserName() {
        return performedByUserName;
    }

    public void setPerformedByUserName(String performedByUserName) {
        this.performedByUserName = performedByUserName;
    }

    public ConsultationHistoriesEventType getEventType() {
        return eventType;
    }

    public void setEventType(ConsultationHistoriesEventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}