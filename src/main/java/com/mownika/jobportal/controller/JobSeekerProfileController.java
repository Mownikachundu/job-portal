package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.JobSeekerProfileDto;
import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.service.JobSeekerProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/jobseeker/profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/create")
    public String createProfileForm(Model model) {

        model.addAttribute("profile", new JobSeekerProfileDto());

        return "create-profile";
    }

    @PostMapping("/create")
    public String createProfile(@ModelAttribute("profile") JobSeekerProfileDto profileDto,
                                Principal principal) {

        String email = principal.getName();

        jobSeekerProfileService.createProfile(profileDto, email);

        return "redirect:/jobseeker/dashboard";
    }

    @GetMapping("/view")
    public String viewProfile(Model model,
                              Principal principal) {

        String email = principal.getName();

        JobSeekerProfile profile = jobSeekerProfileService.getProfile(email);

        model.addAttribute("profile", profile);

        return "view-profile";
    }

    @GetMapping("/edit")
    public String editProfileForm(Model model,
                                  Principal principal) {

        String email = principal.getName();

        JobSeekerProfile profile = jobSeekerProfileService.getProfile(email);

        model.addAttribute("profile", profile);

        return "edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute("profile") JobSeekerProfileDto profileDto,
                                Principal principal) {

        String email = principal.getName();

        jobSeekerProfileService.updateProfile(profileDto, email);

        return "redirect:/jobseeker/profile/view";
    }
}