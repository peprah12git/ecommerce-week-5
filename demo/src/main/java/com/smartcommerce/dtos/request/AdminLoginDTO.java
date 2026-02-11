package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for admin login")
public record AdminLoginDTO(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "Admin email address", example = "admin@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        @Schema(description = "Admin password", example = "admin@123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
