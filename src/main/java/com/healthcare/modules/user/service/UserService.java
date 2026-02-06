package com.healthcare.modules.user.service;

import com.healthcare.modules.auth.dto.LoginResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.dto.RegisterResponseDTO;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface UserService {

    RegisterResponseDTO createUser(RegisterUserDTO registerUserDTO );
    LoginResponseDTO loginUser(LoginUserDTO loginUserDTO);
    UserResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO);
    PageResponse<UserResponseDTO> findAllUsers(int page, int size);
    UserResponseDTO findUserById(UUID id);
    UserResponseDTO findUserByUsername(String username);
    UserResponseDTO findUserByEmail(String email);
    void deleteUser(UUID id);
    UserEntity findUserEntityById(UUID id);
    UserEntity findUserEntityByEmail(String email);

}
