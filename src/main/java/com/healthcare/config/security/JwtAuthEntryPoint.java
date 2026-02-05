package com.healthcare.config.security;

import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorMessage errorMessage = (ErrorMessage) request.getAttribute("JWT_ERROR");

        if (errorMessage == null) {
            errorMessage = ErrorMessage.UNAUTHORIZED;
        }

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        errorMessage.getStatus(),
                        errorMessage.getType(),
                        errorMessage.getMessage(),
                        "SECURITY_EXCEPTION",
                        null,
                        Map.of(
                                "exception", authException.getClass().getSimpleName(),
                                "path", request.getServletPath()
                        )
                );

        response.setStatus(errorMessage.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
