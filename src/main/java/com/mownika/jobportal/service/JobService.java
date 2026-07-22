package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.JobDto;
import com.mownika.jobportal.entity.Job;

import java.util.List;

public interface JobService {

    void createJob(JobDto jobDto, String email);
    List<Job> getCompanyJobs(String email);
    Job getJobById(Long id);
    JobDto getJobForEdit(Long id);
    void updateJob(Long id, JobDto jobDto);
    void deleteJob(Long id);
    List<Job> getAllJobs();

}