
package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.JobSeekerProfileDto;
import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.service.JobSeekerProfileService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobSeekerProfileRestControllerTest {

    @Mock
    private JobSeekerProfileService profileService;

    @InjectMocks
    private JobSeekerProfileRestController profileController;

    private Principal principal() {
        return () -> "seeker@example.com";
    }

    @Test
    void createProfile_success_returnsCreated() {
        JobSeekerProfileDto dto = new JobSeekerProfileDto();

        ResponseEntity<String> response =
                profileController.createProfile(dto, principal());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Profile created successfully", response.getBody());

        verify(profileService).createProfile(dto, "seeker@example.com");
    }

    @Test
    void getProfile_success_returnsProfileResponse() {
        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setPhone("9876543210");
        profile.setAddress("Guntur");
        profile.setEducation("B.Tech");
        profile.setSkills("Java");
        profile.setExperience("Fresher");
        profile.setResumeUrl("resume.pdf");

        when(profileService.getProfile("seeker@example.com"))
                .thenReturn(profile);

        ResponseEntity<?> response =
                profileController.getProfile(principal());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(profileService).getProfile("seeker@example.com");
    }

    @Test
    void updateProfile_success_returnsOk() {
        JobSeekerProfileDto dto = new JobSeekerProfileDto();

        ResponseEntity<String> response =
                profileController.updateProfile(dto, principal());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile updated successfully", response.getBody());

        verify(profileService).updateProfile(dto, "seeker@example.com");
    }
}
