package com.healthcare.modules.user.service;

import com.healthcare.modules.auth.dto.AuthResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;
import com.healthcare.shared.response.PageResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    AuthResponseDTO createUser(RegisterUserDTO registerUserDTO );
    AuthResponseDTO loginUser(LoginUserDTO loginUserDTO);
    UserResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO);
    PageResponse<UserResponseDTO> findAllUsers(int page, int size);
    UserResponseDTO findUserById(UUID id);
    UserResponseDTO findUserByUsername(String username);
    UserResponseDTO findUserByEmail(String email);
    void deleteUser(UUID id);
    UserEntity findUserEntityById(UUID id);
    UserEntity findUserEntityByEmail(String email);

}
