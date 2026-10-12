
package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.JobDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.service.JobService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobRestControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobRestController jobRestController;

    private Principal principal;
    private JobDto jobDto;
    private Job job;

    @BeforeEach
    void setUp() {
        principal = () -> "recruiter@test.com";

        jobDto = new JobDto();
        jobDto.setTitle("Java Developer");
        jobDto.setDescription("Backend development");
        jobDto.setLocation("Remote");

        Company company = new Company();
        company.setId(1L);
        company.setName("Test Company");

        job = new Job();
        job.setId(10L);
        job.setTitle("Java Developer");
        job.setDescription("Backend development");
        job.setLocation("Remote");
        job.setCompany(company);
        job.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createJob_success_returnsCreated() {
        ResponseEntity<String> response =
                jobRestController.createJob(jobDto, principal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Job created successfully", response.getBody());

        verify(jobService).createJob(jobDto, "recruiter@test.com");
    }

    @Test
    void getCompanyJobs_success_returnsJobs() {
        when(jobService.getCompanyJobs("recruiter@test.com"))
                .thenReturn(Collections.singletonList(job));

        ResponseEntity<?> response =
                jobRestController.getCompanyJobs(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getJob_success_returnsJob() {
        when(jobService.getJobById(10L)).thenReturn(job);

        ResponseEntity<?> response = jobRestController.getJob(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllJobs_success_returnsJobs() {
        when(jobService.getAllJobs()).thenReturn(Collections.singletonList(job));

        ResponseEntity<?> response = jobRestController.getAllJobs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateJob_success_returnsOk() {
        ResponseEntity<String> response =
                jobRestController.updateJob(10L, jobDto, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job updated successfully", response.getBody());

        verify(jobService).updateJob(
                10L, jobDto, "recruiter@test.com");
    }

    @Test
    void deleteJob_success_returnsOk() {
        ResponseEntity<String> response =
                jobRestController.deleteJob(10L, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job deleted successfully", response.getBody());

        verify(jobService).deleteJob(10L, "recruiter@test.com");
    }
}

