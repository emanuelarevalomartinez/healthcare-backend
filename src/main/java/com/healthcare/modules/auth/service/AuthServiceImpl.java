package com.healthcare.modules.auth.service;

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
    public UserResponseDTO register(RegisterUserDTO dto) {
        return userService.createUser(dto);
    }
}
