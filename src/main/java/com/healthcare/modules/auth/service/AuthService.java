package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.*;

public interface AuthService {
    Void register(RegisterUserDTO dto);
    LoginResponseDTO login(LoginUserDTO dto);
    RefreshTokenResponseDTO refresh(String refreshToken);
}
