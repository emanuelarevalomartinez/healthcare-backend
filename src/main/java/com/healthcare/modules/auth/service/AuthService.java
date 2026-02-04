package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.AuthResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterUserDTO dto);
    AuthResponseDTO login(LoginUserDTO dto);
}
