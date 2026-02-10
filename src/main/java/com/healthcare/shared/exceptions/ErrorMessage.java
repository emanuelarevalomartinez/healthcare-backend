package com.healthcare.shared.exceptions;

public enum ErrorMessage {
    INTERNAL_ERROR(500, "INTERNAL_ERROR", "Error interno del servidor"),

    EMAIL_CONFLICT(409, "EMAIL_CONFLICT", "El email ya está registrado"),
    USERNAME_CONFLICT(409, "USERNAME_CONFLICT", "El nombre de usuario ya está en uso"),

    USER_NOT_FOUND_ID(404, "USER_NOT_FOUND", "Usuario con id no encontrado"),
    USER_NOT_FOUND_EMAIL(404, "USER_NOT_FOUND", "Usuario con email no encontrado"),
    USER_NOT_FOUND_USERNAME(404, "USER_NOT_FOUND", "Usuario con username no encontrado"),

    INVALID_CREDENTIALS(401, "AUTH_FAILED", "Credenciales inválidas"),
    USER_INACTIVE(403, "USER_INACTIVE", "El usuario no está activado"),

    JWT_EXPIRED(401, "JWT_EXPIRED", "La sesión ha expirado"),
    JWT_MALFORMED(401, "JWT_INVALID", "Token inválido"),
    JWT_UNSUPPORTED(401, "JWT_UNSUPPORTED", "Token no soportado"),
    JWT_INVALID_SIGNATURE(401, "JWT_INVALID_SIGNATURE", "Token no confiable"),
    JWT_EMPTY(401, "JWT_MISSING", "No se proporcionó token"),
    JWT_INVALID_TYPE(401, "JWT_INVALID_TYPE", "Tipo de token inválido"),
    JWT_ACCESS_REQUIRED(401, "JWT_ACCESS_REQUIRED", "Se requiere un access token"),

    UNAUTHORIZED(401, "UNAUTHORIZED", "Acceso no autorizado"),

    PERSISTENCE_ERROR(400, "PERSISTENCE_ERROR", "Error de integridad de datos"),

    PATIENT_NOT_FOUND_ID(404, "PATIENT_NOT_FOUND", "Paciente con id no encontrado"),
    PATIENT_MRN_CONFLICT(409, "PATIENT_MRN_CONFLICT", "Historia clínica ya registrada"),
    PATIENT_DOCUMENT_CONFLICT(409, "PATIENT_DOCUMENT_CONFLICT", "Documento ya registrado"),
    REFRESH_TOKEN_INVALID(401, "REFRESH_TOKEN_INVALID", "Sesión inválida, por favor inicie sesión nuevamente"),
    REFRESH_TOKEN_EXPIRED(401, "REFRESH_TOKEN_EXPIRED", "La sesión ha expirado, por favor inicie sesión nuevamente");


    private final String message;
    private final String type;
    private final int status;

    ErrorMessage() {
        this.status = 409;
        this.type = "APPLICATION_EXCEPTION";
        this.message = "Invalid or poorly formatted data.";
    }

    ErrorMessage(int status, String type, String message) {
        this.status = status;
        this.type = type;
        this.message = message;
    }

    public String getMessage() { return message; }
    public String getType() { return type; }
    public int getStatus() { return status; }

}
