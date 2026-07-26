package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.ApplicationStatus;

import java.util.List;

public interface ApplicationService {

    void applyForJob(Long jobId, String email);

    List<Application> getMyApplications(String email);

    List<Application> getApplicants(Long jobId);

    Application getApplicationById(Long id);

    void updateStatus(Long applicationId, ApplicationStatus status);

}