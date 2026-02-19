package com.smartcommerce.exception;

/**
 * Thrown when authentication (e.g., missing/invalid token) fails.
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

