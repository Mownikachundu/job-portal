package com.mownika.jobportal.repository;

import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerProfileRepository
        extends JpaRepository<JobSeekerProfile, Long> {

    Optional<JobSeekerProfile> findByUser(User user);
}