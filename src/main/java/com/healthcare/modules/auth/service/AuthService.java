package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.*;
import com.healthcare.modules.auth.providers.CustomUserDetails;
import com.healthcare.modules.user.dto.UserWithDoctorResponseDTO;
import com.healthcare.modules.user.enums.UserRole;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AuthService {
    void register(RegisterUserDTO dto);
    LoginResponseDTO login(LoginUserDTO dto);
    UserWithDoctorResponseDTO me(Authentication authentication);
    RefreshTokenResponseDTO refresh(String refreshToken);
    Authentication getAuthentication();
    CustomUserDetails getCurrentUserDetails();
    UUID getCurrentUserId();
    UserRole getCurrentRole();
}
