package com.healthcare.modules.user.service;

import com.healthcare.modules.auth.dto.LoginResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.*;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface UserService {

    void registerUser(RegisterUserDTO registerUserDTO );
    LoginResponseDTO loginUser(LoginUserDTO loginUserDTO);
    UserWithDoctorResponseDTO createUser(CreateUserDTO createUserDTO);
    UserWithDoctorResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO);
    PageResponse<UserWithDoctorResponseDTO> findAllUsers(int page, int size);
    UserWithDoctorResponseDTO findUserById(UUID id);
    UserEntity findUserByUsername(String username);
    UserWithDoctorResponseDTO findUserByEmail(String email);
    void deleteUser(UUID id);
    UserEntity findUserEntityById(UUID id);
    UserEntity findUserEntityByEmail(String email);

}
