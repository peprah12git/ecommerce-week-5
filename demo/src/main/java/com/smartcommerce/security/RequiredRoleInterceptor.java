package com.smartcommerce.security;

import com.smartcommerce.exception.AccessDeniedException;
import com.smartcommerce.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class RequiredRoleInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public RequiredRoleInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true; // static resources or non-controller handler
        }

        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();

        RequiredRole required = method.getAnnotation(RequiredRole.class);
        if (required == null) {
            // check at class level
            required = hm.getBeanType().getAnnotation(RequiredRole.class);
        }

        if (required == null) return true; // no role required

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Authorization header missing or malformed");
        }

        String token = authHeader.substring(7).trim();
        String role;
        try {
            role = jwtUtil.getRoleFromToken(token);
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token");
        }

        if (role == null || !role.equalsIgnoreCase(required.value())) {
            throw new AccessDeniedException("Insufficient privileges");
        }

        // Optionally attach user id to request attributes
        int userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentUserRole", role);

        return true;
    }
}

