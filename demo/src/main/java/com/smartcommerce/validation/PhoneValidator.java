package com.smartcommerce.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link ValidPhone}.
 * Accepts null/blank values (use @NotBlank separately if required).
 * Supports international formats: +1234567890, (123) 456-7890, 123-456-7890, 123.456.7890, etc.
 */
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,20}$"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Let @NotBlank handle null/blank checks
        }
        return PHONE_PATTERN.matcher(value).matches();
    }
}
