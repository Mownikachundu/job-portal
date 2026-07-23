package com.mownika.jobportal.service;

import com.mownika.jobportal.entity.Application;
import java.util.List;

public interface ApplicationService {

    void applyJob(Long jobId, String email);
    List<Application> getMyApplications(String email);

}