package com.smartcommerce.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation error response with per-field error details.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Validation error response with field-level details")
public class ValidationErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error type", example = "Validation Failed")
    private String error;

    @Schema(description = "Summary message", example = "One or more fields have validation errors")
    private String message;

    @Schema(description = "API path that caused the error", example = "/api/products")
    private String path;

    @Schema(description = "Timestamp of the error")
    private LocalDateTime timestamp;

    @Schema(description = "Map of field names to their validation error messages")
    private Map<String, String> fieldErrors;

    // Manual constructor to match Lombok @AllArgsConstructor
    public ValidationErrorResponse(int status, String error, String message, String path, LocalDateTime timestamp, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
        this.fieldErrors = fieldErrors;
    }

    // Manual builder method for compatibility
    public static ValidationErrorResponseBuilder builder() {
        return new ValidationErrorResponseBuilder();
    }

    public static class ValidationErrorResponseBuilder {
        private int status;
        private String error;
        private String message;
        private String path;
        private LocalDateTime timestamp;
        private Map<String, String> fieldErrors;

        public ValidationErrorResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ValidationErrorResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ValidationErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ValidationErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ValidationErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ValidationErrorResponseBuilder fieldErrors(Map<String, String> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public ValidationErrorResponse build() {
            return new ValidationErrorResponse(status, error, message, path, timestamp, fieldErrors);
        }
    }
}
