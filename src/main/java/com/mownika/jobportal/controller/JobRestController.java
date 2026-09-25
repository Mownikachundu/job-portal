package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.JobDto;
import com.mownika.jobportal.dto.JobResponseDto;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class JobRestController {

    private final JobService jobService;

    public JobRestController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/recruiter/jobs")
    public ResponseEntity<String> createJob(
            @RequestBody JobDto jobDto,
            Principal principal) {

        String email = principal.getName();

        jobService.createJob(jobDto, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Job created successfully");
    }

    @GetMapping("/recruiter/jobs")
    public ResponseEntity<List<JobResponseDto>> getCompanyJobs(
            Principal principal) {

        String email = principal.getName();

        List<Job> jobs = jobService.getCompanyJobs(email);

        List<JobResponseDto> response = jobs.stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobResponseDto> getJob(
            @PathVariable Long id) {

        Job job = jobService.getJobById(id);

        return ResponseEntity.ok(convertToResponse(job));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponseDto>> getAllJobs() {

        List<Job> jobs = jobService.getAllJobs();

        List<JobResponseDto> response = jobs.stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/recruiter/jobs/{id}")
    public ResponseEntity<String> updateJob(
            @PathVariable Long id,
            @RequestBody JobDto jobDto,
            Principal principal) {

        String email = principal.getName();

        jobService.updateJob(id, jobDto, email);

        return ResponseEntity.ok("Job updated successfully");
    }

    @DeleteMapping("/recruiter/jobs/{id}")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long id,
            Principal principal) {

        String email = principal.getName();

        jobService.deleteJob(id, email);

        return ResponseEntity.ok("Job deleted successfully");
    }

    private JobResponseDto convertToResponse(Job job) {

        return new JobResponseDto(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getSalary(),
                job.getLocation(),
                job.getExperienceRequired(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCreatedAt()
        );
    }
}