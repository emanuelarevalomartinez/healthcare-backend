package com.healthcare.modules.user.controller;

import com.healthcare.modules.user.dto.*;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import com.healthcare.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserWithDoctorAndSchedulesResponseDTO>> createPatient(@Valid @RequestBody CreateUserDTO createUserDTO) {

        UserWithDoctorAndSchedulesResponseDTO user = userService.createUser(createUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created user",
                user
        );
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserEntity>> findUserByUsername(@PathVariable String username) {

        UserEntity user = this.userService.findUserByUsername(username);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                user
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserWithDoctorAndSchedulesResponseDTO>> findAllUsers(@PathVariable String email) {

        UserWithDoctorAndSchedulesResponseDTO user = this.userService.findUserByEmail(email);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                user
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserWithDoctorAndSchedulesResponseDTO>>> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<UserWithDoctorAndSchedulesResponseDTO> users = userService.findAllUsers(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                users
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserWithDoctorAndSchedulesResponseDTO>> findUserById(@PathVariable UUID id) {

        UserWithDoctorAndSchedulesResponseDTO user = userService.findUserById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                user
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserWithDoctorAndSchedulesResponseDTO>> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {

        UserWithDoctorAndSchedulesResponseDTO userUpdate = this.userService.updateUser(id, updateUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "User updated successfully",
                userUpdate
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteUserById(@PathVariable UUID id) {

        this.userService.deleteUser(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete user",
                null
        );
    }

}
