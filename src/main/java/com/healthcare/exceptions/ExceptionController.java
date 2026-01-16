package com.healthcare.exceptions;

import com.healthcare.config.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleApplicationExceptions(ApplicationException ex) {
        int statusCode = (ex.getStatusCode() > 0) ? ex.getStatusCode() : HttpStatus.BAD_REQUEST.value();

        ApiResponse<Map<String, String>> response = new ApiResponse<Map<String, String>>(
                statusCode,
                ex.getType(),
                ex.getMessage(),
                "BUSINESS_EXCEPTION",
                (Map<String, String>) ex.getData(),
                Map.of("exception", ex.getClass().getSimpleName())
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()
                ))
                .toList();

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "ValidationError",
                "Los datos enviados no son válidos",
                "VALIDATION_EXCEPTION",
                null,
                Map.of("errors", errors)

        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleEmptyOrInvalidDtoBody(HttpMessageNotReadableException ex) {
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "InvalidRequestBody",
                "El cuerpo de la solicitud está vacío o mal formado",
                "REQUEST_EXCEPTION",
                null,
                Map.of("error", ex.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> buildErrorInfo(Throwable ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("exception", ex.getClass().getSimpleName());
        error.put("message", ex.getMessage());
        if (ex.getCause() != null) {
            error.put("cause", ex.getCause().getClass().getSimpleName() + ": " + ex.getCause().getMessage());
        }
        return error;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnexpected(Exception ex) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Internal error occurred",
                "GLOBAL_EXCEPTION",
                null,
                buildErrorInfo(ex)
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
