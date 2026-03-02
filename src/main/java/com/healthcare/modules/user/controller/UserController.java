package com.healthcare.modules.user.controller;

import com.healthcare.modules.user.dto.UpdateUserIsActiveRequestDTO;
import com.healthcare.modules.user.dto.UpdateUserPasswordRequestDTO;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findUserByUsername(@PathVariable String username) {

        UserResponseDTO user = this.userService.findUserByUsername(username);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                user
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findAllUsers(@PathVariable String email) {

        UserResponseDTO user = this.userService.findUserByEmail(email);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                user
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDTO>>> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<UserResponseDTO> users = userService.findAllUsers(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                users
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable UUID id , @Valid @RequestBody UpdateUserDTO updateUserDTO) {

        UserResponseDTO userUpdate = this.userService.updateUser(id, updateUserDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "User updated successfully",
                userUpdate
        );
    }
    @PutMapping("changePassword/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> changePassword(@PathVariable UUID id , @Valid @RequestBody UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO) {

        this.userService.changePassword(id, updateUserPasswordRequestDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "User password update",
                null
        );
    }

    @PutMapping("changeIsActive/{id}")
    public ResponseEntity<ApiResponse<Boolean>> changeUserIsActive(@PathVariable UUID id , @Valid @RequestBody UpdateUserIsActiveRequestDTO updateUserIsActiveRequestDTO) {

        boolean newStatus = this.userService.changeUserIsActiveStatus(id, updateUserIsActiveRequestDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "User status changed",
                newStatus
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
