package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.AuthResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public AuthResponseDTO register(RegisterUserDTO registerUserDTO) {
        return userService.createUser(registerUserDTO);
    }

    @Override
    public AuthResponseDTO login(LoginUserDTO loginUserDTO) {
        return userService.loginUser(loginUserDTO);
    }
}
