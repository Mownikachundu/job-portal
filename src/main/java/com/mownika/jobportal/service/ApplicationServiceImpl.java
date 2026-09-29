package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.ApplicationStatus;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.exception.BadRequestException;
import com.mownika.jobportal.exception.ResourceNotFoundException;
import com.mownika.jobportal.repository.ApplicationRepository;
import com.mownika.jobportal.repository.JobRepository;
import com.mownika.jobportal.repository.RecruiterProfileRepository;
import com.mownika.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository,
            RecruiterProfileRepository recruiterProfileRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public void applyForJob(Long jobId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found"));

        if (applicationRepository
                .existsByJobAndUser(job, user)) {

            throw new BadRequestException(
                    "You have already applied for this job");
        }

        Application application =
                new Application();

        application.setJob(job);
        application.setUser(user);
        application.setStatus(
                ApplicationStatus.APPLIED);
        application.setAppliedDate(
                LocalDateTime.now());

        applicationRepository.save(application);
    }

    @Override
    public List<Application> getMyApplications(
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        return applicationRepository
                .findByUser(user);
    }

    @Override
    public List<Application> getApplicants(
            Long jobId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Recruiter not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found"));

        if (!job.getCompany().getId()
                .equals(recruiterProfile
                        .getCompany().getId())) {

            throw new RuntimeException(
                    "You are not allowed to view applicants for this job");
        }

        return applicationRepository.findByJob(job);
    }

    @Override
    public Application getApplicationById(Long id) {

        return applicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found"));
    }

    @Override
    public void updateStatus(
            Long applicationId,
            ApplicationStatus status,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Recruiter not found"));

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found"));

        if (!application.getJob()
                .getCompany()
                .getId()
                .equals(recruiterProfile
                        .getCompany()
                        .getId())) {

            throw new RuntimeException(
                    "You are not allowed to update this application");
        }

        application.setStatus(status);

        applicationRepository.save(application);
    }
}