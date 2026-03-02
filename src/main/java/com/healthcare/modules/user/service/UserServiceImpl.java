package com.healthcare.modules.user.service;

import com.healthcare.config.security.JwtGenerator;
import com.healthcare.modules.auth.dto.LoginResponseDTO;
import com.healthcare.modules.auth.dto.LoginUserDTO;
import com.healthcare.modules.auth.service.RefreshTokenService;
import com.healthcare.modules.user.dto.UpdateUserPasswordRequestDTO;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.repository.UserRepository;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
    public Void createUser(RegisterUserDTO registerUserDto) {

        try{

            if (userRepository.findByUsername(registerUserDto.username()).isPresent()) {
                throw new ApplicationException(ErrorMessage.USERNAME_CONFLICT, registerUserDto.username());
            }

            if (userRepository.findByEmail(registerUserDto.email()).isPresent()) {
                throw new ApplicationException(ErrorMessage.EMAIL_CONFLICT,  registerUserDto.email());
            }

            UserEntity newUser = new UserEntity();
            newUser.setUsername(registerUserDto.username());
            newUser.setEmail(registerUserDto.email());
            newUser.setPasswordHash(passwordEncoder.encode(registerUserDto.password()));
            newUser.setRole(registerUserDto.role());
            newUser.setActive(false);

            this.userRepository.save(newUser);

        }  catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
        return null;
    }

    @Override
    public LoginResponseDTO loginUser(LoginUserDTO loginUserDTO) {
        try {

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
                        ErrorMessage.USER_INACTIVE,""
                );
            }

            String accessToken = jwtGenerator.generateAccessToken(user);
            String refreshToken = refreshTokenService.createAndSaveRefreshToken(user);

            LoginResponseDTO response = new LoginResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.isActive(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    user.getLastLogin(),
                    accessToken,
                    refreshToken
            );

            return response;

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO) {

        try {

            UserEntity findUser = this.findUserEntityById(id);

            if (updateUserDTO.username() != null) {
                findUser.setUsername(updateUserDTO.username());
            }

            if (updateUserDTO.email() != null) {
                findUser.setEmail(updateUserDTO.email());
            }
            // hay que validar que el usuario coloque la password previa o eso seria un endpoint por aparte ?

            if (updateUserDTO.password() != null) {
                findUser.setPasswordHash(passwordEncoder.encode(updateUserDTO.password()));
            }

            if (updateUserDTO.role() != null) {
                findUser.setRole(updateUserDTO.role());
            }

            UserEntity userUpdated = this.userRepository.save(findUser);

            return UserResponseDTO.fromEntity(userUpdated);

        } catch(ApplicationException ex){
                throw ex;
            } catch(Exception ex){
                throw new ApplicationException(ex);
            }
    }

    @Override
    public PageResponse<UserResponseDTO> findAllUsers(int page, int size) {

        try{

            Pageable pageable = PageRequest.of(page, size);
            Page<UserEntity> result = userRepository.findAllUsersPaged(pageable);

            return new PageResponse<>(
                    result.getContent()
                            .stream()
                            .map(UserResponseDTO::fromEntity)
                            .toList(),
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages()
            );

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public UserResponseDTO findUserById(UUID id) {
        try{

            UserEntity findUserById = this.userRepository.findById(id)
                    .orElseThrow( () -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_ID, id)
            );

            return UserResponseDTO.fromEntity(findUserById);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public UserResponseDTO findUserByUsername(String username) {
        try{

            UserEntity findUserByUsername = this.userRepository.findByUsername(username)
                    .orElseThrow( () -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_USERNAME, username)
                    );

            return UserResponseDTO.fromEntity(findUserByUsername);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        try{

            UserEntity findUserByEmail = this.userRepository.findByEmail(email)
                    .orElseThrow( () -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_EMAIL, email)
                    );

            return UserResponseDTO.fromEntity(findUserByEmail);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }


    @Override
    public void deleteUser(UUID id) {

        try {
            UserEntity user = this.findUserEntityById(id);
            userRepository.deleteById(user.getId());

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
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

    @Override
    public void changePassword(UUID userId, UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO) {
        try{
            UserEntity user = this.findUserEntityById(userId);

            if(updateUserPasswordRequestDTO.previousPassword().isEmpty() || updateUserPasswordRequestDTO.newPassword().isEmpty()){
                throw new ApplicationException(
                        ErrorMessage.INVALID_PASSWORD_CHANGE_REQUEST, ""
                );
            }

            boolean passwordMatch = passwordEncoder.matches(
                    updateUserPasswordRequestDTO.previousPassword(),
                    user.getPasswordHash()
            );

            if (!passwordMatch) {
                throw new ApplicationException(
                        ErrorMessage.INVALID_CHANGE_PASSWORD, updateUserPasswordRequestDTO.previousPassword()
                );
            }

            user.setPasswordHash(passwordEncoder.encode(updateUserPasswordRequestDTO.newPassword()));
            this.userRepository.save(user);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }


}
