package com.mownika.jobportal.controller;

import com.mownika.jobportal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
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
        String name = userService.getUserNameByEmail(email);
        model.addAttribute("name", name);

        return "jobseeker-dashboard";
    }
}