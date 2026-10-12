
package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Role;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_userExists_returnsUserDetails() {
        User user = new User();
        user.setEmail("recruiter@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.RECRUITER);

        when(userRepository.findByEmail("recruiter@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                customUserDetailsService.loadUserByUsername(
                        "recruiter@example.com");

        assertEquals("recruiter@example.com", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("RECRUITER")));

        verify(userRepository).findByEmail("recruiter@example.com");
    }

    @Test
    void loadUserByUsername_userDoesNotExist_throwsException() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(
                        "missing@example.com")
        );

        verify(userRepository).findByEmail("missing@example.com");
    }
}
