package com.healthcare.shared.exceptions;

public enum ErrorMessage {
    DEFAULT_APPLICATION_ERROR,
    EMAIL_CONFLICT( 409, "EmailConflict", "El email ya está registrado: "),
    USERNAME_CONFLICT( 409, "UsernameConflict", "El nombre de usuario ya está en uso: "),
    USER_NOT_FOUND_ID( 404, "UserNotFound", "Usuario con id no encontrado: "),
    USER_NOT_FOUND_EMAIL( 404, "UserNotFound", "Usuario con email no encontrado: "),
    USER_NOT_FOUND_USERNAME( 404, "UserNotFound", "Usuario con username no encontrado: "),
    INVALID_CREDENTIALS(401, "AuthFailed", "Credenciales inválidas"),
    PERSISTENCE_ERROR(400, "UserPersistenceError", "Error de integridad de datos"),
    PATIENT_NOT_FOUND_ID( 404, "PatientNotFound", "Paciente con id no encontrado: "),
    PATIENT_MRN_CONFLICT(409, "PatientMRNConflict", "El número de historia clínica ya está registrado: "),
    PATIENT_DOCUMENT_CONFLICT(409, "PatientDocumentConflict", "El número de documento ya está registrado: "),
    JWT_EXPIRED(401, "JWT_EXPIRED", "El token ha expirado"),
    JWT_MALFORMED(401, "JWT_MALFORMED", "El token es inválido"),
    JWT_UNSUPPORTED(401, "JWT_UNSUPPORTED", "El token no es soportado"),
    JWT_INVALID_SIGNATURE(401, "JWT_INVALID_SIGNATURE", "Firma del token inválida"),
    JWT_EMPTY(401, "JWT_EMPTY", "El token es nulo o vacío"),
    UNAUTHORIZED(401, "UNAUTHORIZED", "Acceso no autorizado"),
    USER_INACTIVE( 409, "USER_INACTIVE", "El usuario no esta activado "),
    ;


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
