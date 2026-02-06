package com.smartcommerce.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom validation annotation to ensure a sort direction value is either ASC or DESC.
 * Usage: @ValidSortDirection on a String field or parameter.
 */
@Documented
@Constraint(validatedBy = SortDirectionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortDirection {

    String message() default "Sort direction must be 'ASC' or 'DESC'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
