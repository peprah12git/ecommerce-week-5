package com.smartcommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcommerce.dtos.request.AdminLoginDTO;
import com.smartcommerce.dtos.response.LoginResponse;
import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ErrorResponse;
import com.smartcommerce.exception.ValidationErrorResponse;
import com.smartcommerce.model.User;
import com.smartcommerce.service.serviceInterface.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin authentication and management API")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Admin login", description = "Authenticates an admin user with email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or not an admin",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> adminLogin(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        LoginResponse response = userService.login(adminLoginDTO.email(), adminLoginDTO.password());
        
        if (!"ADMIN".equalsIgnoreCase(response.user().getRole())) {
            throw new BusinessException("Access denied. Admin privileges required.");
        }
        
        return ResponseEntity.ok(response);
    }
}
