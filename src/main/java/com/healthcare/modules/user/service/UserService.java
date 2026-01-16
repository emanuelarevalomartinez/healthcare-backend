package com.healthcare.modules.user.service;

import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.enums.UserRole;

import java.util.UUID;

public interface UserService {

    UserResponseDTO createUser( RegisterUserDTO registerUserDTO );
    void deleteUser(UUID id);

}
