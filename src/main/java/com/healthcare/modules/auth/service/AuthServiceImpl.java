package com.healthcare.modules.auth.service;

import com.healthcare.config.security.JwtGenerator;
import com.healthcare.config.security.SecurityConstants;
import com.healthcare.modules.auth.dto.*;
import com.healthcare.modules.auth.entity.RefreshTokenEntity;
import com.healthcare.modules.auth.repository.RefreshTokenRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import com.healthcare.config.security.JwtGenerator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenServiceImpl refreshTokenService;

    public AuthServiceImpl(UserService userService, JwtGenerator jwtGenerator, RefreshTokenRepository refreshTokenRepository, RefreshTokenServiceImpl refreshTokenService) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Void register(RegisterUserDTO registerUserDTO) {
        return userService.createUser(registerUserDTO);
    }

    @Override
    public LoginResponseDTO login(LoginUserDTO loginUserDTO) {
        return userService.loginUser(loginUserDTO);
    }

    @Override
    public RefreshTokenResponseDTO refresh(String refreshToken) {

        try{

            RefreshTokenResponseDTO save = this.refreshTokenService.validateAndSaveNewRefreshToken(refreshToken);

            return new RefreshTokenResponseDTO(
                    save.accessToken(),
                    save.refreshToken()
            );

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }
}
