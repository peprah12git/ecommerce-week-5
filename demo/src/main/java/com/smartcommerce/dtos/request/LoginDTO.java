package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for user login")
public record LoginDTO(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "User's email address", example = "b.elsifie@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        @Schema(description = "User's password", example = "bernard@123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}