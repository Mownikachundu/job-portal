
package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.JobSeekerProfileDto;
import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.exception.BadRequestException;
import com.mownika.jobportal.exception.ResourceNotFoundException;
import com.mownika.jobportal.repository.JobSeekerProfileRepository;
import com.mownika.jobportal.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobSeekerProfileServiceImplTest {

    @Mock
    private JobSeekerProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobSeekerProfileServiceImpl profileService;

    private User user;
    private JobSeekerProfileDto dto;
    private final String email = "seeker@example.com";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(email);

        dto = new JobSeekerProfileDto();
        dto.setPhone("9876543210");
        dto.setAddress("Guntur");
        dto.setEducation("B.Tech");
        dto.setSkills("Java, Spring Boot");
        dto.setExperience("Fresher");
        dto.setResumeUrl("resume.pdf");
    }

    @Test
    void profileExists_profilePresent_returnsTrue() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.of(new JobSeekerProfile()));

        assertTrue(profileService.profileExists(email));
    }

    @Test
    void profileExists_profileAbsent_returnsFalse() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertFalse(profileService.profileExists(email));
    }

    @Test
    void profileExists_userMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.profileExists(email));
    }

    @Test
    void createProfile_success_savesProfile() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        profileService.createProfile(dto, email);

        ArgumentCaptor<JobSeekerProfile> captor =
                ArgumentCaptor.forClass(JobSeekerProfile.class);

        verify(profileRepository).save(captor.capture());

        JobSeekerProfile saved = captor.getValue();

        assertSame(user, saved.getUser());
        assertEquals("9876543210", saved.getPhone());
        assertEquals("Guntur", saved.getAddress());
        assertEquals("B.Tech", saved.getEducation());
        assertEquals("Java, Spring Boot", saved.getSkills());
        assertEquals("Fresher", saved.getExperience());
        assertEquals("resume.pdf", saved.getResumeUrl());
    }

    @Test
    void createProfile_userMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.createProfile(dto, email));

        verify(profileRepository, never())
                .save(any(JobSeekerProfile.class));
    }

    @Test
    void createProfile_alreadyExists_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.of(new JobSeekerProfile()));

        assertThrows(BadRequestException.class,
                () -> profileService.createProfile(dto, email));

        verify(profileRepository, never())
                .save(any(JobSeekerProfile.class));
    }

    @Test
    void getProfile_success_returnsProfile() {
        JobSeekerProfile profile = new JobSeekerProfile();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        assertSame(profile, profileService.getProfile(email));
    }

    @Test
    void getProfile_profileMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.getProfile(email));
    }

    @Test
    void updateProfile_success_updatesAndSavesProfile() {
        JobSeekerProfile profile = new JobSeekerProfile();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        profileService.updateProfile(dto, email);

        assertEquals("9876543210", profile.getPhone());
        assertEquals("Guntur", profile.getAddress());
        assertEquals("B.Tech", profile.getEducation());
        assertEquals("Java, Spring Boot", profile.getSkills());
        assertEquals("Fresher", profile.getExperience());
        assertEquals("resume.pdf", profile.getResumeUrl());

        verify(profileRepository).save(profile);
    }

    @Test
    void updateProfile_profileMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> profileService.updateProfile(dto, email));

        verify(profileRepository, never())
                .save(any(JobSeekerProfile.class));
    }
}
