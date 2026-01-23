package com.healthcare.shared.response;

public class ApiResponse<T> {
    private int status;
    private String type;
    private String message;
    private String origin;
    private T data;
    private T error;

    public ApiResponse(int status, String type, String message, String origin, T data, T error) {
        this.status = status;
        this.type = type;
        this.message = message;
        this.origin = origin;
        this.data = data;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }
}
