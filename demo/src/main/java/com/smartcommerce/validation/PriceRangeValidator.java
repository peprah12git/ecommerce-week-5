package com.smartcommerce.validation;

import com.smartcommerce.dtos.request.ProductFilterDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link ValidPriceRange}.
 * Ensures that when both minPrice and maxPrice are provided,
 * minPrice does not exceed maxPrice.
 */
public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, ProductFilterDTO> {

    @Override
    public boolean isValid(ProductFilterDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.minPrice() == null || dto.maxPrice() == null) {
            return true; // Skip if either is null
        }
        boolean valid = dto.minPrice().compareTo(dto.maxPrice()) <= 0;
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Minimum price (" + dto.minPrice() + ") must not exceed maximum price (" + dto.maxPrice() + ")"
            ).addPropertyNode("minPrice").addConstraintViolation();
        }
        return valid;
    }
}
