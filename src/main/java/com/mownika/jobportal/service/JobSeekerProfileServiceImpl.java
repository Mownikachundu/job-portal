package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.JobSeekerProfileDto;
import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.JobSeekerProfileRepository;
import com.mownika.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerProfileServiceImpl implements JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;

    public JobSeekerProfileServiceImpl(JobSeekerProfileRepository jobSeekerProfileRepository,
                                       UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean profileExists(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jobSeekerProfileRepository.findByUser(user).isPresent();
    }

    @Override
    public void createProfile(JobSeekerProfileDto dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeekerProfile profile = new JobSeekerProfile();

        profile.setUser(user);
        profile.setPhone(dto.getPhone());
        profile.setAddress(dto.getAddress());
        profile.setEducation(dto.getEducation());
        profile.setSkills(dto.getSkills());
        profile.setExperience(dto.getExperience());
        profile.setResumeUrl(dto.getResumeUrl());

        jobSeekerProfileRepository.save(profile);
    }

    @Override
    public JobSeekerProfile getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jobSeekerProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public void updateProfile(JobSeekerProfileDto dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeekerProfile profile = jobSeekerProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPhone(dto.getPhone());
        profile.setAddress(dto.getAddress());
        profile.setEducation(dto.getEducation());
        profile.setSkills(dto.getSkills());
        profile.setExperience(dto.getExperience());
        profile.setResumeUrl(dto.getResumeUrl());

        jobSeekerProfileRepository.save(profile);
    }
}