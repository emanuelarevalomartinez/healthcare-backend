package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.*;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RefreshTokenServiceImpl refreshTokenService;

    public AuthServiceImpl(UserService userService, RefreshTokenServiceImpl refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void register(RegisterUserDTO registerUserDTO) {
         userService.registerUser(registerUserDTO);
    }

    @Override
    public LoginResponseDTO login(LoginUserDTO loginUserDTO) {
        return userService.loginUser(loginUserDTO);
    }

    @Override
    public RefreshTokenResponseDTO refresh(String refreshToken) {

        RefreshTokenResponseDTO save = this.refreshTokenService.validateAndSaveNewRefreshToken(refreshToken);

        return new RefreshTokenResponseDTO(
                save.accessToken(),
                save.refreshToken()
        );
    }
}
