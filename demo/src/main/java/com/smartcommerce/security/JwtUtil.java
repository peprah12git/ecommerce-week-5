package com.smartcommerce.security;

import com.smartcommerce.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    
    private final SecretKey key = Keys.hmacShaKeyFor("SmartCommerceSecretKeyForJWTTokenGeneration12345".getBytes());
    private final long expiration = 86400000; // 24 hours

    public String generateToken(User user) {
        return generateToken(user.getUserId(), user.getEmail(), user.getRole());
    }

    public String generateToken(int userId, String email, String role) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public int getUserId(String token) {
        return Integer.parseInt(validateToken(token).getSubject());
    }

    public String getRole(String token) {
        return validateToken(token).get("role", String.class);
    }

    public String getRoleFromToken(String token) {
        return getRole(token);
    }

    public int getUserIdFromToken(String token) {
        return getUserId(token);
    }
}
