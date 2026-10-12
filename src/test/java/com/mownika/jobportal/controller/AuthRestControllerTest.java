
package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.LoginDto;
import com.mownika.jobportal.dto.RegisterUserDto;
import com.mownika.jobportal.service.JwtService;
import com.mownika.jobportal.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthRestController authRestController;

    @Test
    void registerUser_success_returnsCreated() {
        RegisterUserDto dto = new RegisterUserDto();

        ResponseEntity<String> response =
                authRestController.registerUser(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(userService).registerUser(dto);
    }

    @Test
    void registerUser_serviceFails_throwsException() {
        RegisterUserDto dto = new RegisterUserDto();

        doThrow(new RuntimeException("Registration failed"))
                .when(userService).registerUser(dto);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authRestController.registerUser(dto)
        );

        assertEquals("Registration failed", exception.getMessage());
    }

    @Test
    void login_success_returnsToken() {
        LoginDto dto = new LoginDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("test@example.com")
                        .password("encodedPassword")
                        .authorities("JOB_SEEKER")
                        .build();

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails))
                .thenReturn("sample-jwt-token");

        ResponseEntity<String> response = authRestController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sample-jwt-token", response.getBody());
    }

    @Test
    void login_invalidCredentials_throwsException() {
        LoginDto dto = new LoginDto();
        dto.setEmail("test@example.com");
        dto.setPassword("wrongPassword");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(
                BadCredentialsException.class,
                () -> authRestController.login(dto)
        );

        verify(jwtService, never()).generateToken(any(UserDetails.class));
    }
}
