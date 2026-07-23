package com.mownika.jobportal.repository;

import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByJobAndUser(Job job, User user);
    List<Application> findByJob(Job job);
    List<Application> findByUser(User user);

}
