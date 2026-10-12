
package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.JobDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.exception.ForbiddenException;
import com.mownika.jobportal.exception.ResourceNotFoundException;
import com.mownika.jobportal.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecruiterProfileRepository recruiterProfileRepository;

    @Mock
    private JobSeekerProfileRepository jobSeekerProfileRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private User user;
    private Company company;
    private RecruiterProfile recruiter;
    private Job job;
    private JobDto jobDto;

    @BeforeEach
    void setUp() {
        user = new User();

        company = new Company();
        company.setId(1L);
        company.setName("Test Company");

        recruiter = new RecruiterProfile();
        recruiter.setCompany(company);

        job = new Job();
        job.setId(10L);
        job.setTitle("Java Developer");
        job.setDescription("Backend development");
        job.setLocation("Remote");
        job.setCompany(company);
        job.setRecruiter(recruiter);

        jobDto = new JobDto();
        jobDto.setTitle("Java Developer");
        jobDto.setDescription("Backend development");
        jobDto.setLocation("Remote");
    }

    private void mockRecruiterFound() {
        when(userRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(user));

        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));
    }

    @Test
    void createJob_success_savesJob() {
        mockRecruiterFound();

        jobService.createJob(jobDto, "recruiter@test.com");

        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void createJob_userMissing_throwsException() {
        when(userRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.createJob(jobDto, "recruiter@test.com"));

        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void createJob_recruiterMissing_throwsException() {
        when(userRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(user));

        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.createJob(jobDto, "recruiter@test.com"));
    }

    @Test
    void getCompanyJobs_success_returnsJobs() {
        mockRecruiterFound();
        when(jobRepository.findByCompany(company))
                .thenReturn(Collections.singletonList(job));

        List<Job> result =
                jobService.getCompanyJobs("recruiter@test.com");

        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());
    }

    @Test
    void getJobForEdit_success_returnsDto() {
        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        JobDto result = jobService.getJobForEdit(10L);

        assertEquals("Java Developer", result.getTitle());
        assertEquals("Backend development", result.getDescription());
    }

    @Test
    void getJobForEdit_jobMissing_throwsException() {
        when(jobRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.getJobForEdit(10L));
    }

    @Test
    void updateJob_success_updatesJob() {
        mockRecruiterFound();
        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        jobDto.setTitle("Senior Java Developer");

        jobService.updateJob(10L, jobDto, "recruiter@test.com");

        assertEquals("Senior Java Developer", job.getTitle());
        verify(jobRepository).save(job);
    }

    @Test
    void updateJob_jobMissing_throwsException() {
        mockRecruiterFound();
        when(jobRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.updateJob(
                        10L, jobDto, "recruiter@test.com"));
    }

    @Test
    void updateJob_otherCompany_throwsForbiddenException() {
        mockRecruiterFound();

        Company otherCompany = new Company();
        otherCompany.setId(2L);
        job.setCompany(otherCompany);

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        assertThrows(ForbiddenException.class,
                () -> jobService.updateJob(
                        10L, jobDto, "recruiter@test.com"));

        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void deleteJob_success_deletesJob() {
        mockRecruiterFound();
        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        jobService.deleteJob(10L, "recruiter@test.com");

        verify(jobRepository).delete(job);
    }

    @Test
    void deleteJob_otherCompany_throwsForbiddenException() {
        mockRecruiterFound();

        Company otherCompany = new Company();
        otherCompany.setId(2L);
        job.setCompany(otherCompany);

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        assertThrows(ForbiddenException.class,
                () -> jobService.deleteJob(
                        10L, "recruiter@test.com"));

        verify(jobRepository, never()).delete(any(Job.class));
    }

    @Test
    void getAllJobs_success_returnsJobs() {
        when(jobRepository.findAll()).thenReturn(Collections.singletonList(job));

        List<Job> result = jobService.getAllJobs();

        assertEquals(1, result.size());
        verify(jobRepository).findAll();
    }

    @Test
    void getJobById_success_returnsJob() {
        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        Job result = jobService.getJobById(10L);

        assertEquals("Java Developer", result.getTitle());
    }

    @Test
    void getJobById_jobMissing_throwsException() {
        when(jobRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> jobService.getJobById(10L));
    }
}
