package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.RegisterUserDto;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.Role;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.exception.BadRequestException;
import com.mownika.jobportal.repository.JobSeekerProfileRepository;
import com.mownika.jobportal.repository.RecruiterProfileRepository;
import com.mownika.jobportal.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecruiterProfileRepository recruiterProfileRepository;

    @Mock
    private JobSeekerProfileRepository jobSeekerProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterUserDto dto;

    @BeforeEach
    void setUp() {
        dto = new RegisterUserDto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setConfirmPassword("password123");
        dto.setRole(Role.JOB_SEEKER);
    }

    @Test
    void registerUser_jobSeeker_success() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        userService.registerUser(dto);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.JOB_SEEKER, savedUser.getRole());

        verify(recruiterProfileRepository, never())
                .save(any(RecruiterProfile.class));
    }

    @Test
    void registerUser_recruiter_createsRecruiterProfile() {
        dto.setRole(Role.RECRUITER);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        userService.registerUser(dto);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        ArgumentCaptor<RecruiterProfile> profileCaptor =
                ArgumentCaptor.forClass(RecruiterProfile.class);

        verify(recruiterProfileRepository)
                .save(profileCaptor.capture());

        assertSame(userCaptor.getValue(),
                profileCaptor.getValue().getUser());
    }

    @Test
    void registerUser_emailAlreadyExists_throwsException() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                BadRequestException.class,
                () -> userService.registerUser(dto)
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(recruiterProfileRepository, never())
                .save(any(RecruiterProfile.class));
    }

    @Test
    void registerUser_passwordsDoNotMatch_throwsException() {
        dto.setConfirmPassword("differentPassword");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                BadRequestException.class,
                () -> userService.registerUser(dto)
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }


    @Test
    void getUserNameByEmail_userExists_returnsName() {
        User user = new User();
        user.setName("Test User");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        String result =
                userService.getUserNameByEmail("test@example.com");

        assertEquals("Test User", result);
    }

    @Test
    void getUserNameByEmail_userDoesNotExist_throwsException() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserNameByEmail("missing@example.com")
        );

        assertEquals("User not found", exception.getMessage());
    }

}
