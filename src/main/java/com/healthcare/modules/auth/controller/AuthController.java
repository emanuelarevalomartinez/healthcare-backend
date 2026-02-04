package com.healthcare.modules.auth.controller;


import com.healthcare.modules.auth.dto.AuthResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
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
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {

        AuthResponseDTO user = authService.register(registerUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created user",
                user
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {

        AuthResponseDTO user = authService.login(loginUserDTO);

        String message = null;

        if(user.token() == null){
            message = "User is not active";
        }

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                message,
                user
        );
    }

}
