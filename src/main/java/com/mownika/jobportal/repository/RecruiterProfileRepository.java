package com.mownika.jobportal.repository;

import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterProfileRepository
        extends JpaRepository<RecruiterProfile, Long> {

    Optional<RecruiterProfile> findByUser(User user);

}