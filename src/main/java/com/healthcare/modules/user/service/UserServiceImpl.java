package com.healthcare.modules.user.service;

import com.healthcare.exceptions.ApplicationException;
import com.healthcare.exceptions.ErrorMessage;
import com.healthcare.modules.auth.dto.RegisterUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        newUser.setActive(true);

        UserEntity saved = this.userRepository.save(newUser);
        return UserResponseDTO.fromEntity(saved);
    }

    @Override
    public void deleteUser(UUID id) {

        try {

            if (!userRepository.existsById(id)) {
                throw new ApplicationException(ErrorMessage.USER_NOT_FOUND_ID, id);
            }
            userRepository.deleteById(id);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex){
            throw new ApplicationException(ex);
        }
    }
}
