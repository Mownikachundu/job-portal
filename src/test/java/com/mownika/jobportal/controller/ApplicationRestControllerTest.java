
package com.mownika.jobportal.controller;

import com.mownika.jobportal.entity.*;
import com.mownika.jobportal.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationRestControllerTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationRestController applicationRestController;

    private Principal userPrincipal;
    private Principal recruiterPrincipal;
    private Application application;

    @BeforeEach
    void setUp() {
        userPrincipal = () -> "user@test.com";
        recruiterPrincipal = () -> "recruiter@test.com";

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("user@test.com");

        Job job = new Job();
        job.setId(10L);
        job.setTitle("Java Developer");

        application = new Application();
        application.setId(100L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedDate(LocalDateTime.now());
    }

    @Test
    void applyForJob_success_returnsCreated() {
        ResponseEntity<String> response =
                applicationRestController.applyForJob(10L, userPrincipal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Application submitted successfully", response.getBody());

        verify(applicationService).applyForJob(10L, "user@test.com");
    }

    @Test
    void getMyApplications_success_returnsApplications() {
        when(applicationService.getMyApplications("user@test.com"))
                .thenReturn(Collections.singletonList(application));

        ResponseEntity<?> response =
                applicationRestController.getMyApplications(userPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getApplicants_success_returnsApplicants() {
        when(applicationService.getApplicants(10L, "recruiter@test.com"))
                .thenReturn(Collections.singletonList(application));

        ResponseEntity<?> response =
                applicationRestController.getApplicants(10L, recruiterPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateStatus_success_returnsOk() {
        ResponseEntity<String> response =
                applicationRestController.updateStatus(
                        100L, ApplicationStatus.REJECTED, recruiterPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Application status updated successfully",
                response.getBody());

        verify(applicationService).updateStatus(
                100L, ApplicationStatus.REJECTED, "recruiter@test.com");
    }

    @Test
    void getMyApplications_emptyList_returnsOk() {
        when(applicationService.getMyApplications("user@test.com"))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response =
                applicationRestController.getMyApplications(userPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getApplicants_emptyList_returnsOk() {
        when(applicationService.getApplicants(10L, "recruiter@test.com"))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response =
                applicationRestController.getApplicants(10L, recruiterPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
