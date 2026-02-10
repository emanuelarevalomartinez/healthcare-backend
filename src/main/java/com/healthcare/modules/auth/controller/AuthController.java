package com.healthcare.modules.auth.controller;


import com.healthcare.config.security.JwtGenerator;
import com.healthcare.modules.auth.dto.*;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
import com.healthcare.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService, JwtGenerator jwtGenerator) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {

        authService.register(registerUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created user",
                null
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {

        LoginResponseDTO user = authService.login(loginUserDTO);

       /* String message = null;

        if(user.token() == null){
            message = "User is not active";
        }*/

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                null,
                user
        );
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponseDTO> refresh(
            @Valid @RequestBody RefreshTokenDTO refreshTokenRequestDTO
    ) {
        RefreshTokenResponseDTO response = authService.refresh(refreshTokenRequestDTO.refreshToken());

        return new ApiResponse<>(
                200,
                "AUTH_REFRESH_SUCCESS",
                null,
                "AUTH",
                response,
                null
        );
    }


}
