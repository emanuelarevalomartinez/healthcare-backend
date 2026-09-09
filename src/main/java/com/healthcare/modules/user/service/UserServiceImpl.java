package com.healthcare.modules.user.service;

import com.healthcare.config.security.JwtGenerator;
import com.healthcare.modules.auth.dto.LoginResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.service.RefreshTokenService;
import com.healthcare.modules.user.dto.*;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.repository.UserRepository;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenService refreshTokenService;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtGenerator jwtGenerator, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

        this.jwtGenerator = jwtGenerator;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void registerUser(RegisterUserDTO registerUserDto) {

        if (userRepository.findByUsername(registerUserDto.username()).isPresent()) {
            throw new ApplicationException(ErrorMessage.USERNAME_CONFLICT, "");
        }

        if (userRepository.findByEmail(registerUserDto.email()).isPresent()) {
            throw new ApplicationException(ErrorMessage.EMAIL_CONFLICT, "");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerUserDto.username());
        newUser.setEmail(registerUserDto.email());
        newUser.setPasswordHash(passwordEncoder.encode(registerUserDto.password()));
        newUser.setRole(registerUserDto.role());
        newUser.setActive(false);

        this.userRepository.save(newUser);
    }

    @Override
    public LoginResponseDTO loginUser(LoginUserDTO loginUserDTO) {

        UserEntity user = this.findUserEntityByEmail(loginUserDTO.email());

        boolean passwordMatch = passwordEncoder.matches(
                loginUserDTO.password(),
                user.getPasswordHash()
        );

        if (!passwordMatch) {
            throw new ApplicationException(
                    ErrorMessage.INVALID_CREDENTIALS, ""
            );
        }

        if (!user.isActive()) {
            throw new ApplicationException(
                    ErrorMessage.USER_INACTIVE, ""
            );
        }

        String accessToken = jwtGenerator.generateAccessToken(user);
        String refreshToken = refreshTokenService.createAndSaveRefreshToken(user);

        user.setLastLogin(LocalDateTime.now());
        UserEntity userSaved = this.userRepository.save(user);

       return LoginResponseDTO.fromEntity(userSaved, accessToken, refreshToken);
    }

    @Override
    public UserWithDoctorResponseDTO createUser(CreateUserDTO createUserDTO) {

        if (userRepository.findByUsername(createUserDTO.username()).isPresent()) {
            throw new ApplicationException(ErrorMessage.USERNAME_CONFLICT, "");
        }

        if (userRepository.findByEmail(createUserDTO.email()).isPresent()) {
            throw new ApplicationException(ErrorMessage.EMAIL_CONFLICT, "");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(createUserDTO.username());
        newUser.setEmail(createUserDTO.email());
        newUser.setPasswordHash(passwordEncoder.encode(createUserDTO.password()));
        newUser.setRole(createUserDTO.role());
        newUser.setActive(createUserDTO.isActive());

        UserEntity userSaved = this.userRepository.save(newUser);
        return UserWithDoctorResponseDTO.fromEntity(userSaved);
    }

    @Override
    public UserWithDoctorResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO) {

        UserEntity findUser = this.findUserEntityById(id);

        if (updateUserDTO.username() != null) {
            findUser.setUsername(updateUserDTO.username());
        }

        if (updateUserDTO.email() != null) {
            findUser.setEmail(updateUserDTO.email());
        }

        if (updateUserDTO.password() != null) {
            findUser.setPasswordHash(passwordEncoder.encode(updateUserDTO.password()));
        }

        if (updateUserDTO.role() != null) {
            findUser.setRole(updateUserDTO.role());
        }

        if (updateUserDTO.isActive() != null) {
            findUser.setActive(updateUserDTO.isActive());
        }

        UserEntity userUpdated = this.userRepository.save(findUser);
        return UserWithDoctorResponseDTO.fromEntity(userUpdated);
    }

    @Override
    public PageResponse<UserWithDoctorResponseDTO> findAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserEntity> result = userRepository.findAllUsersPaged(pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(UserWithDoctorResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public UserWithDoctorResponseDTO findUserById(UUID id) {

        UserEntity findUserById = this.userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_ID, "")
                );

        return UserWithDoctorResponseDTO.fromEntity(findUserById);
    }

    @Override
    public UserEntity findUserByUsername(String username) {

        UserEntity findUserByUsername = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_USERNAME, "")
                );

        return findUserByUsername;
    }

    @Override
    public UserWithDoctorResponseDTO findUserByEmail(String email) {

        UserEntity findUserByEmail = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_EMAIL, "")
                );

        return UserWithDoctorResponseDTO.fromEntity(findUserByEmail);
    }


    @Override
    public void deleteUser(UUID id) {
        UserEntity user = this.findUserEntityById(id);
        userRepository.deleteById(user.getId());
    }

    @Override
    public UserEntity findUserEntityById(UUID id) {

        return this.userRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.USER_NOT_FOUND_ID, id);
                });
    }

    @Override
    public UserEntity findUserEntityByEmail(String email) {

        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.USER_NOT_FOUND_EMAIL, email);
                });
    }

}
