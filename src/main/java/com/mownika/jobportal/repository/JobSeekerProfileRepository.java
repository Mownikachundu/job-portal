package com.mownika.jobportal.repository;

import com.mownika.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerProfileRepository
        extends JpaRepository<JobSeekerProfile, Long> {

}