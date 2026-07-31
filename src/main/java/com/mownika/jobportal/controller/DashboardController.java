package com.mownika.jobportal.controller;

import com.mownika.jobportal.service.JobSeekerProfileService;
import com.mownika.jobportal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public DashboardController(UserService userService,
                               JobSeekerProfileService jobSeekerProfileService) {
        this.userService = userService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard(Model model,
                                     Principal principal) {

        String email = principal.getName();
        String name = userService.getUserNameByEmail(email);
        model.addAttribute("name", name);

        return "recruiter-dashboard";
    }

    @GetMapping("/jobseeker/dashboard")
    public String jobSeekerDashboard(Model model,
                                     Principal principal) {

        String email = principal.getName();

        if (!jobSeekerProfileService.profileExists(email)) {
            return "redirect:/jobseeker/profile/create";
        }

        String name = userService.getUserNameByEmail(email);
        model.addAttribute("name", name);

        return "jobseeker-dashboard";
    }
}