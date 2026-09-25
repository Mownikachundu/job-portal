package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.JobSeekerProfileDto;
import com.mownika.jobportal.dto.JobSeekerProfileResponseDto;
import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.service.JobSeekerProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/jobseeker/profile")
public class JobSeekerProfileRestController {

    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerProfileRestController(
            JobSeekerProfileService jobSeekerProfileService) {

        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @PostMapping
    public ResponseEntity<String> createProfile(
            @RequestBody JobSeekerProfileDto profileDto,
            Principal principal) {

        String email = principal.getName();

        jobSeekerProfileService.createProfile(profileDto, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Profile created successfully");
    }

    @GetMapping
    public ResponseEntity<JobSeekerProfileResponseDto> getProfile(
            Principal principal) {

        String email = principal.getName();

        JobSeekerProfile profile =
                jobSeekerProfileService.getProfile(email);

        JobSeekerProfileResponseDto response =
                new JobSeekerProfileResponseDto(
                        profile.getId(),
                        profile.getPhone(),
                        profile.getAddress(),
                        profile.getEducation(),
                        profile.getSkills(),
                        profile.getExperience(),
                        profile.getResumeUrl()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<String> updateProfile(
            @RequestBody JobSeekerProfileDto profileDto,
            Principal principal) {

        String email = principal.getName();

        jobSeekerProfileService.updateProfile(profileDto, email);

        return ResponseEntity.ok("Profile updated successfully");
    }
}