package com.healthcare.modules.user.enums;

public enum UserRole {
    ADMIN("Administrador", "Acceso completo al sistema"),
    DOCTOR("Médico", "Puede ver citas y registrar consultas"),
    RECEPTIONIST("Recepcionista", "Puede gestionar pacientes y citas");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
