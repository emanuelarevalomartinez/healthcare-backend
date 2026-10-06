package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.RefreshTokenResponseDTO;
import com.healthcare.modules.users.entity.UserEntity;

public interface RefreshTokenService {
    String createAndSaveRefreshToken(UserEntity user);
    RefreshTokenResponseDTO validateAndSaveNewRefreshToken(String refreshToken);
}
