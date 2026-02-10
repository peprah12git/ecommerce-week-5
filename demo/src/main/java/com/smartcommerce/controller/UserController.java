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
        User userToCreate = new User(
                createUserDTO.name(),
                createUserDTO.email(),
                createUserDTO.password(),
                createUserDTO.phone(),
                createUserDTO.address()
        );
        User user = userService.createUser(userToCreate);
        UserResponse response = new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getCreatedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
