package com.healthcare.modules.doctor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(
        name = "doctor"
)
public class DoctorEntity {

    @Id
    @UuidGenerator
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @Column(name = "specialty", nullable = false, length = 100)
    private String specialty;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(name = "default_consultation_duration")
    private Integer defaultConsultationDuration;

    @Column(name = "is_active")
    private Boolean isActive;

    public DoctorEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getDefaultConsultationDuration() {
        return defaultConsultationDuration;
    }

    public void setDefaultConsultationDuration(Integer defaultConsultationDuration) {
        this.defaultConsultationDuration = defaultConsultationDuration;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
