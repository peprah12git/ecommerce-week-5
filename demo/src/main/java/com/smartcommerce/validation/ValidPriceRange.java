package com.smartcommerce.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Class-level custom validation annotation that ensures minPrice <= maxPrice
 * when both are provided on a filter DTO.
 * Usage: @ValidPriceRange on a record/class with minPrice and maxPrice fields.
 */
@Documented
@Constraint(validatedBy = PriceRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriceRange {

    String message() default "Minimum price must not exceed maximum price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
