package com.healthcare.modules.auth.service;

import com.healthcare.config.security.JwtGenerator;
import com.healthcare.modules.auth.dto.*;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import org.springframework.stereotype.Service;
import com.healthcare.config.security.JwtGenerator;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final JwtGenerator jwtGenerator;

    public AuthServiceImpl(UserService userService, JwtGenerator jwtGenerator) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public RegisterResponseDTO register(RegisterUserDTO registerUserDTO) {
        return userService.createUser(registerUserDTO);
    }

    @Override
    public LoginResponseDTO login(LoginUserDTO loginUserDTO) {
        return userService.loginUser(loginUserDTO);
    }

    @Override
    public RefreshTokenResponseDTO refresh(String refreshToken) {

        if (!jwtGenerator.validateToken(refreshToken, null)) {
            throw new ApplicationException(ErrorMessage.JWT_EXPIRED, "");
        }

        if (!jwtGenerator.isRefreshToken(refreshToken)) {
            throw new ApplicationException(ErrorMessage.JWT_INVALID_TYPE, "");
        }

        String email = jwtGenerator.getUsernameFromJWT(refreshToken);

        var user = userService.findUserEntityByEmail(email);

        String newAccessToken = jwtGenerator.generateAccessToken(user);

        return new RefreshTokenResponseDTO(
                newAccessToken,
                refreshToken
        );
    }
}
