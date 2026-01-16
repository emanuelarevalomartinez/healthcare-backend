package com.healthcare.modules.auth.service;

import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(RegisterUserDTO dto);
}
