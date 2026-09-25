package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.ApplicationResponseDto;
import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.ApplicationStatus;
import com.mownika.jobportal.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationRestController {

    private final ApplicationService applicationService;

    public ApplicationRestController(
            ApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<String> applyForJob(
            @PathVariable Long jobId,
            Principal principal) {

        String email = principal.getName();

        applicationService.applyForJob(jobId, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Application submitted successfully");
    }

    @GetMapping("/jobseeker/applications")
    public ResponseEntity<List<ApplicationResponseDto>>
    getMyApplications(Principal principal) {

        String email = principal.getName();

        List<Application> applications =
                applicationService.getMyApplications(email);

        List<ApplicationResponseDto> response =
                applications.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruiter/jobs/{jobId}/applications")
    public ResponseEntity<List<ApplicationResponseDto>>
    getApplicants(
            @PathVariable Long jobId,
            Principal principal) {

        String email = principal.getName();

        List<Application> applications =
                applicationService.getApplicants(jobId, email);

        List<ApplicationResponseDto> response =
                applications.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/recruiter/applications/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            Principal principal) {

        String email = principal.getName();

        applicationService.updateStatus(
                id, status, email);

        return ResponseEntity.ok(
                "Application status updated successfully");
    }

    private ApplicationResponseDto convertToResponse(
            Application application) {

        return new ApplicationResponseDto(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getUser().getId(),
                application.getUser().getName(),
                application.getUser().getEmail(),
                application.getStatus(),
                application.getAppliedDate()
        );
    }
}