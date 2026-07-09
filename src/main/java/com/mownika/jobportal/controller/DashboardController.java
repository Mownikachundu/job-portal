package com.mownika.jobportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/jobseeker/dashboard")
    public String jobSeekerDashboard() {
        return "jobseeker-dashboard";
    }

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard() {
        return "recruiter-dashboard";
    }
}