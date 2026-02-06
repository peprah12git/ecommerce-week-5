package com.smartcommerce.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom validation annotation for phone number format.
 * Accepts common formats: +1234567890, (123) 456-7890, 123-456-7890, etc.
 * Usage: @ValidPhone on a String field.
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

    String message() default "Invalid phone number format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
