package com.healthcare.modules.auth.controller;


import com.healthcare.config.response.ApiResponse;
import com.healthcare.config.response.ResponseHandler;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {

        UserResponseDTO user = authService.register(registerUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created user",
                user
        );
    }

}
