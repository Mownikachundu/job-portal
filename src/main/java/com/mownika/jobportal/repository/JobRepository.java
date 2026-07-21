package com.mownika.jobportal.repository;

import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompany(Company company);

}