package com.healthcare.modules.user.service;

import com.healthcare.exceptions.ApplicationException;
import com.healthcare.exceptions.ErrorMessage;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(RegisterUserDTO registerUserDto) {

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerUserDto.username());
        newUser.setEmail(registerUserDto.email());
        newUser.setPasswordHash(registerUserDto.password());
        newUser.setRole(registerUserDto.role());
        newUser.setActive(false);

        UserEntity saved = this.userRepository.save(newUser);
        return UserResponseDTO.fromEntity(saved);
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

            if (updateUserDTO.password() != null) {
                findUser.setPasswordHash(updateUserDTO.password());
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
    public List<UserResponseDTO> findAllUsers() {
        return this.userRepository
                .findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
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

    private UserEntity findUserEntityById(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.USER_NOT_FOUND_ID, id));
    }
}
