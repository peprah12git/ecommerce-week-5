package com.smartcommerce.validation;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link ValidSortDirection}.
 * Accepts null values (use @NotNull separately if required).
 * Case-insensitive comparison against ASC and DESC.
 */
public class SortDirectionValidator implements ConstraintValidator<ValidSortDirection, String> {

    private static final Set<String> VALID_DIRECTIONS = Set.of("ASC", "DESC");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null checks
        }
        return VALID_DIRECTIONS.contains(value.toUpperCase());
    }
}
