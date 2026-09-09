package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.*;
import com.healthcare.modules.auth.providers.CustomUserDetails;
import com.healthcare.modules.user.dto.UserWithDoctorResponseDTO;
import com.healthcare.modules.user.enums.UserRole;
import com.healthcare.modules.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;


    public AuthServiceImpl(UserService userService, RefreshTokenService refreshTokenService) {
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
    public UserWithDoctorResponseDTO me(Authentication authentication) {
        return userService.findUserByEmail(authentication.getName());
    }

    @Override
    public RefreshTokenResponseDTO refresh(String refreshToken) {

        RefreshTokenResponseDTO save = this.refreshTokenService.validateAndSaveNewRefreshToken(refreshToken);

        return new RefreshTokenResponseDTO(
                save.accessToken(),
                save.refreshToken()
        );
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    @Override
    public UUID getCurrentUserId() {
        return getCurrentUserDetails().getId();
    }

    @Override
    public UserRole getCurrentRole() {

        String authority = getCurrentUserDetails()
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Authenticated user has no role")
                )
                .getAuthority();

        return UserRole.valueOf(
                authority.replace("ROLE_", "")
        );
    }

}
