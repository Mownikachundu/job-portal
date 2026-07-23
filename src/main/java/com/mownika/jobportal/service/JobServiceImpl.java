package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.JobDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;


    public JobServiceImpl(JobRepository jobRepository,
                          UserRepository userRepository,
                          RecruiterProfileRepository recruiterProfileRepository, JobSeekerProfileRepository jobSeekerProfileRepository,
                          ApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public void createJob(JobDto jobDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter not found"));

        Company company = recruiterProfile.getCompany();

        Job job = new Job();

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setSalary(jobDto.getSalary());
        job.setLocation(jobDto.getLocation());
        job.setExperienceRequired(jobDto.getExperienceRequired());

        job.setCompany(company);
        job.setRecruiter(recruiterProfile);
        job.setCreatedAt(LocalDateTime.now());

        jobRepository.save(job);
    }

    @Override
    public List<Job> getCompanyJobs(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter not found"));

        Company company = recruiterProfile.getCompany();

        return jobRepository.findByCompany(company);
    }

    @Override
    public Job getJobById(Long id) {

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));
    }

    @Override
    public JobDto getJobForEdit(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        JobDto jobDto = new JobDto();

        jobDto.setTitle(job.getTitle());
        jobDto.setDescription(job.getDescription());
        jobDto.setSalary(job.getSalary());
        jobDto.setLocation(job.getLocation());
        jobDto.setExperienceRequired(job.getExperienceRequired());

        return jobDto;
    }

    @Override
    public void updateJob(Long id, JobDto jobDto) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setSalary(jobDto.getSalary());
        job.setLocation(jobDto.getLocation());
        job.setExperienceRequired(jobDto.getExperienceRequired());

        jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        jobRepository.delete(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

}