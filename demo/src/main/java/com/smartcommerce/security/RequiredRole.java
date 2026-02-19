package com.smartcommerce.security;

import java.lang.annotation.*;

/**
 * Annotation to require a single role on a controller or handler method.
 * Usage: @RequiredRole("ADMIN")
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiredRole {
    String value();
}
