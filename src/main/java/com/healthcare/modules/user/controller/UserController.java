package com.healthcare.modules.user.controller;

import com.healthcare.config.response.ApiResponse;
import com.healthcare.config.response.ResponseHandler;
import com.healthcare.modules.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
