
package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.ApplicationStatus;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.exception.BadRequestException;
import com.mownika.jobportal.exception.ForbiddenException;
import com.mownika.jobportal.exception.ResourceNotFoundException;
import com.mownika.jobportal.repository.ApplicationRepository;
import com.mownika.jobportal.repository.JobRepository;
import com.mownika.jobportal.repository.RecruiterProfileRepository;
import com.mownika.jobportal.repository.UserRepository;
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
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecruiterProfileRepository recruiterProfileRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private User user;
    private Company company;
    private RecruiterProfile recruiter;
    private Job job;
    private Application application;

    @BeforeEach
    void setUp() {
        user = new User();

        company = new Company();
        company.setId(1L);

        recruiter = new RecruiterProfile();
        recruiter.setCompany(company);

        job = new Job();
        job.setId(10L);
        job.setTitle("Java Developer");
        job.setCompany(company);

        application = new Application();
        application.setId(100L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
    }

    private void mockUserFound() {
        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));
    }

    private void mockRecruiterFound() {
        when(userRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(user));

        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));
    }

    @Test
    void applyForJob_success_savesApplication() {
        mockUserFound();

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        when(applicationRepository.existsByJobAndUser(job, user))
                .thenReturn(false);

        applicationService.applyForJob(10L, "user@test.com");

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void applyForJob_userMissing_throwsException() {
        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.applyForJob(
                        10L, "user@test.com"));
    }

    @Test
    void applyForJob_jobMissing_throwsException() {
        mockUserFound();

        when(jobRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.applyForJob(
                        10L, "user@test.com"));
    }

    @Test
    void applyForJob_alreadyApplied_throwsException() {
        mockUserFound();

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        when(applicationRepository.existsByJobAndUser(job, user))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> applicationService.applyForJob(
                        10L, "user@test.com"));

        verify(applicationRepository, never())
                .save(any(Application.class));
    }

    @Test
    void getMyApplications_success_returnsApplications() {
        mockUserFound();

        when(applicationRepository.findByUser(user))
                .thenReturn(Collections.singletonList(application));

        List<Application> result =
                applicationService.getMyApplications("user@test.com");

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
    }

    @Test
    void getMyApplications_userMissing_throwsException() {
        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.getMyApplications(
                        "user@test.com"));
    }

    @Test
    void getApplicants_success_returnsApplicants() {
        mockRecruiterFound();

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        when(applicationRepository.findByJob(job))
                .thenReturn(Collections.singletonList(application));

        List<Application> result =
                applicationService.getApplicants(
                        10L, "recruiter@test.com");

        assertEquals(1, result.size());
        verify(applicationRepository).findByJob(job);
    }

    @Test
    void getApplicants_otherCompany_throwsForbiddenException() {
        mockRecruiterFound();

        Company otherCompany = new Company();
        otherCompany.setId(2L);
        job.setCompany(otherCompany);

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        assertThrows(ForbiddenException.class,
                () -> applicationService.getApplicants(
                        10L, "recruiter@test.com"));

        verify(applicationRepository, never())
                .findByJob(any(Job.class));
    }

    @Test
    void getApplicants_recruiterMissing_throwsException() {
        when(userRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(user));

        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.getApplicants(
                        10L, "recruiter@test.com"));
    }

    @Test
    void getApplicationById_success_returnsApplication() {
        when(applicationRepository.findById(100L))
                .thenReturn(Optional.of(application));

        Application result =
                applicationService.getApplicationById(100L);

        assertEquals(100L, result.getId());
    }

    @Test
    void getApplicationById_missing_throwsException() {
        when(applicationRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.getApplicationById(100L));
    }

    @Test
    void updateStatus_success_updatesAndSavesApplication() {
        mockRecruiterFound();

        when(applicationRepository.findById(100L))
                .thenReturn(Optional.of(application));

        applicationService.updateStatus(
                100L,
                ApplicationStatus.REJECTED,
                "recruiter@test.com");

        assertEquals(ApplicationStatus.REJECTED, application.getStatus());
        verify(applicationRepository).save(application);
    }

    @Test
    void updateStatus_otherCompany_throwsForbiddenException() {
        mockRecruiterFound();

        Company otherCompany = new Company();
        otherCompany.setId(2L);
        job.setCompany(otherCompany);

        when(applicationRepository.findById(100L))
                .thenReturn(Optional.of(application));

        assertThrows(ForbiddenException.class,
                () -> applicationService.updateStatus(
                        100L,
                        ApplicationStatus.REJECTED,
                        "recruiter@test.com"));

        verify(applicationRepository, never())
                .save(any(Application.class));
    }

    @Test
    void updateStatus_applicationMissing_throwsException() {
        mockRecruiterFound();

        when(applicationRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.updateStatus(
                        100L,
                        ApplicationStatus.REJECTED,
                        "recruiter@test.com"));
    }

    @Test
    void updateStatus_recruiterMissing_throwsException() {
        when(userRepository.findByEmail("recruiter@test.com"))
                .thenReturn(Optional.of(user));

        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.updateStatus(
                        100L,
                        ApplicationStatus.REJECTED,
                        "recruiter@test.com"));
    }
}

