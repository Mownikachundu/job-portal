package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.ApplicationStatus;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.ApplicationRepository;
import com.mownika.jobportal.repository.JobRepository;
import com.mownika.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  JobRepository jobRepository,
                                  UserRepository userRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void applyForJob(Long jobId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        if (applicationRepository.existsByJobAndUser(job, user)) {
            throw new RuntimeException("You have already applied for this job");
        }

        Application application = new Application();

        application.setJob(job);
        application.setUser(user);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedDate(LocalDateTime.now());

        applicationRepository.save(application);
    }

    @Override
    public List<Application> getMyApplications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return applicationRepository.findByUser(user);
    }
}