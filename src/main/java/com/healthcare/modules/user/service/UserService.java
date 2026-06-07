package com.healthcare.modules.user.service;

import com.healthcare.modules.auth.dto.LoginResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.*;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface UserService {

    Void registerUser(RegisterUserDTO registerUserDTO );
    LoginResponseDTO loginUser(LoginUserDTO loginUserDTO);
    UserResponseDTO createUser(CreateUserDTO createUserDTO);
    UserResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO);
    PageResponse<UserResponseDTO> findAllUsers(int page, int size);
    UserResponseDTO findUserById(UUID id);
    UserResponseDTO findUserByUsername(String username);
    UserResponseDTO findUserByEmail(String email);
    void deleteUser(UUID id);
    UserEntity findUserEntityById(UUID id);
    UserEntity findUserEntityByEmail(String email);
    void changePassword(UUID userId, UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO);
    boolean changeUserIsActiveStatus(UUID userId, UpdateUserIsActiveRequestDTO updateUserIsActiveRequestDTO);

}
