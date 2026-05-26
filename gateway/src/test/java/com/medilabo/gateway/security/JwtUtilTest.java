package com.medilabo.gateway.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil.
 * Uses ReflectionTestUtils to inject the jwt.secret value since it is normally
 * provided via @Value. The secret is a Base64-encoded 256-bit key (32 bytes),
 * which is the minimum size required for the HS256 algorithm.
 */
class JwtUtilTest {

    // Base64 encoding of "testSecretKeyForMediLaboApp12345" (exactly 32 bytes / 256 bits)
    private static final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvck1lZGlMYWJvQXBwMTIzNDU=";

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);

        userDetails = new User("admin", "admin123", List.of());
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        assertEquals("admin", username);
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void validateToken_shouldReturnFalse_whenUsernameDoesNotMatch() {
        String token = jwtUtil.generateToken(userDetails);
        UserDetails differentUser = new User("other", "pass", List.of());
        assertFalse(jwtUtil.validateToken(token, differentUser));
    }

    @Test
    void extractExpiration_shouldReturnFutureDate() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }
}
