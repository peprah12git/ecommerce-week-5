package com.smartcommerce.controller;

import com.smartcommerce.dtos.request.CreateUserDTO;
import com.smartcommerce.dtos.response.UserResponse;
import com.smartcommerce.model.User;
import com.smartcommerce.service.serviceInterface.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> addUser(
            @Valid @RequestBody CreateUserDTO createUserDTO
    ) {
        User response = userService.createUser(createUserDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
