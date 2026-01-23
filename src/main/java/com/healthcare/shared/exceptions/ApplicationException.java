package com.healthcare.shared.exceptions;

import java.util.Map;

public class ApplicationException extends RuntimeException {

    private final String message;
    private final String type;
    private final int statusCode;
    private final String origin;
    private final Object data;
    private final Object error;

    public ApplicationException(Exception ex) {
        ErrorMessage defaultError = ErrorMessage.DEFAULT_APPLICATION_ERROR;

        this.statusCode = defaultError.getStatus();
        this.type = defaultError.getType();
        this.origin = this.detectOriginClass();
        this.message = String.format(defaultError.getMessage());
        this.data  = null;
        this.error = Map.of(
                "exception", ex.getClass().getSimpleName()
        );
    }

    public ApplicationException(ErrorMessage err, Object data) {
        this.statusCode = err.getStatus();
        this.type = err.getType();
        this.origin = this.detectOriginClass();
        this.message = String.format(err.getMessage() + data);
        this.data  = null;
        this.error = null;
    }

    public ApplicationException(ErrorMessage err, Exception ex) {
        this.statusCode = err.getStatus();
        this.type = err.getType();
        this.origin = this.detectOriginClass();
        this.message = String.format(err.getMessage());
        this.data  = null;
        this.error = Map.of(
                "exception", ex.getClass().getSimpleName()
        );
    }

    private String detectOriginClass() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            if (!element.getClassName().equals(Thread.class.getName())
                    && !element.getClassName().equals(ApplicationException.class.getName())) {
                return element.getFileName() != null ? element.getFileName() : element.getClassName();
            }
        }
        return "UnknownOrigin";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getOrigin() {
        return origin;
    }

    public Object getData() {
        return data;
    }

    public Object getError() {
        return error;
    }
}