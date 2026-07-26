package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.JobSeekerProfileDto;
import com.mownika.jobportal.entity.JobSeekerProfile;

public interface JobSeekerProfileService {

    boolean profileExists(String email);

    void createProfile(JobSeekerProfileDto dto, String email);

    JobSeekerProfile getProfile(String email);

    void updateProfile(JobSeekerProfileDto dto, String email);

}