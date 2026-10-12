
package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        userDetails = User.withUsername("test@example.com")
                .password("encodedPassword")
                .authorities(Role.JOB_SEEKER.name())
                .build();
    }

    @Test
    void generateToken_returnsNonEmptyToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_returnsCorrectUsername() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void isTokenValid_correctUser_returnsTrue() {
        String token = jwtService.generateToken(userDetails);

        boolean result = jwtService.isTokenValid(token, userDetails);

        assertTrue(result);
    }

    @Test
    void isTokenValid_differentUser_returnsFalse() {
        String token = jwtService.generateToken(userDetails);

        UserDetails anotherUser = User.withUsername("other@example.com")
                .password("encodedPassword")
                .authorities(Role.JOB_SEEKER.name())
                .build();

        boolean result = jwtService.isTokenValid(token, anotherUser);

        assertFalse(result);
    }

    @Test
    void extractUsername_invalidToken_throwsException() {
        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername("invalid-token")
        );
    }
}
