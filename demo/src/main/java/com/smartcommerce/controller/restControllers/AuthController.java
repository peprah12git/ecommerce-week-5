package com.smartcommerce.controller.restControllers;

import com.smartcommerce.dtos.request.CreateUserDTO;
import com.smartcommerce.dtos.request.LoginDTO;
import com.smartcommerce.dtos.response.LoginResponse;
import com.smartcommerce.dtos.response.UserResponse;
import com.smartcommerce.exception.ErrorResponse;
import com.smartcommerce.exception.ValidationErrorResponse;
import com.smartcommerce.model.User;
import com.smartcommerce.service.serviceInterface.UserService;
import com.smartcommerce.utils.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller exposing login and register endpoints
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication endpoints: login and register")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "User login", description = "Authenticates a user with email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponse response = userService.login(loginDTO.email(), loginDTO.password());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserDTO createUserDTO) {
        User userToCreate = new User(
                createUserDTO.name(),
                createUserDTO.email(),
                createUserDTO.password(),
                createUserDTO.phone(),
                createUserDTO.address()
        );
        User created = userService.createUser(userToCreate);
        UserResponse response = UserMapper.toUserResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

