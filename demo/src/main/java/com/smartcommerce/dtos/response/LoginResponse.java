package com.smartcommerce.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body for successful login")
public record LoginResponse(

        @Schema(description = "Authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Token type", example = "Bearer")
        String tokenType,

        @Schema(description = "Token expiration time in milliseconds", example = "1640995200000")
        Long expiresAt,

        @Schema(description = "User information")
        UserResponse user
) {
    public LoginResponse(String token, UserResponse user) {
        this(token, "Bearer", System.currentTimeMillis() + 24 * 60 * 60 * 1000L, user); // 24 hours
    }
}