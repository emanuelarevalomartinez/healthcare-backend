package com.healthcare.modules.auth.controller;


import com.healthcare.config.security.JwtGenerator;
import com.healthcare.modules.auth.dto.*;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.ResponseHandler;
import com.healthcare.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtGenerator jwtGenerator;

    public AuthController(AuthService authService, JwtGenerator jwtGenerator) {
        this.authService = authService;
        this.jwtGenerator = jwtGenerator;
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


 /*   @PostMapping("/auth/refresh")
    public AuthResponseDTO refresh(HttpServletRequest request) {

        String refreshToken = getJwtFromRequest(request);

        if (!jwtGenerator.validateToken(refreshToken, request)) {
            throw new ApplicationException(ErrorMessage.UNAUTHORIZED);
        }

        String email = jwtGenerator.getUsernameFromJWT(refreshToken);
        UserEntity user = userService.findUserEntityByEmail(email);

        return new AuthResponseDTO(
                jwtGenerator.generateAccessToken(user),
                jwtGenerator.generateRefreshToken(user)
        );
    }*/

 /*   @PostMapping("/refresh")
    public RefreshTokenResponseDTO refresh(HttpServletRequest request) {

        String refreshToken = getJwtFromRequest(request);

        if (!jwtGenerator.validateToken(refreshToken, request)) {
            throw new ApplicationException(ErrorMessage.UNAUTHORIZED);
        }

        String email = jwtGenerator.getUsernameFromJWT(refreshToken);
        UserEntity user = userService.findUserEntityByEmail(email);

        return new RefreshTokenResponseDTO(
                jwtGenerator.generateAccessToken(user),
                jwtGenerator.generateRefreshToken(user)
        );
    }*/

    @PostMapping("/refresh")
    public ApiResponse<LoginResponseDTO> refresh(
            @RequestHeader("Authorization") String bearerToken
    ) {

        String token = bearerToken.substring(7);

        LoginResponseDTO response = authService.refresh(token);

        return new ApiResponse<>(
                200,
                "SUCCESS",
                "Token refrescado correctamente",
                "AUTH",
                response,
                null
        );
    }


}
