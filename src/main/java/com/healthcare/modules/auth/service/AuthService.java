package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.*;
import com.healthcare.modules.user.dto.UserResponseDTO;
import org.springframework.security.core.Authentication;

public interface AuthService {
    void register(RegisterUserDTO dto);
    LoginResponseDTO login(LoginUserDTO dto);
    UserResponseDTO me(Authentication authentication);
    RefreshTokenResponseDTO refresh(String refreshToken);
}
