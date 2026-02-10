package com.smartcommerce.dtos.request;

import com.smartcommerce.validation.ValidPhone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for updating an existing user")
public record UpdateUserDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @Schema(description = "Updated full name", example = "Jane Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Schema(description = "Updated email address", example = "jane.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Size(min = 6, max = 128, message = "Password must be between 6 and 128 characters")
        @Schema(description = "New password (optional, min 6 characters)", example = "newSecurePass456")
        String password,

        @ValidPhone
        @Schema(description = "Updated phone number", example = "+1 (555) 987-6543")
        String phone,

        @Size(max = 500, message = "Address cannot exceed 500 characters")
        @Schema(description = "Updated mailing address", example = "456 Oak Ave, Chicago, IL 60601")
        String address,

        @Pattern(regexp = "^(CUSTOMER|ADMIN)$", message = "Role must be either CUSTOMER or ADMIN")
        @Schema(description = "User role", example = "CUSTOMER", allowableValues = {"CUSTOMER", "ADMIN"})
        String role
) {
}