package com.healthcare.shared.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static <T> ResponseEntity<ApiResponse<T>> generateResponse(
            HttpStatus status, HttpHeaders headers, String type, String origin, String message, T data, T error) {

        ApiResponse<T> response = new ApiResponse<>(
                status.value(),
                type,
                message,
                origin,
                data,
                error
        );
        return new ResponseEntity<>(response, headers, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> generateResponse(
            HttpStatus status, HttpHeaders headers, String message, T data) {

        ApiResponse<T> response = new ApiResponse<>(
                status.value(),
                null,
                message,
                null,
                data,
                null
        );
        return new ResponseEntity<>(response, headers, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> generateResponse(HttpStatus status, String message, T data) {

        ApiResponse<T> response = new ApiResponse<>(
                status.value(),
                null,
                message,
                null,
                data,
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> generateResponse(String message, T data) {

        ApiResponse<T> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                message,
                null,
                data,
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
