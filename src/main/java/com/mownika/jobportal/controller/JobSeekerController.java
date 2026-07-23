package com.mownika.jobportal.controller;

import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.service.ApplicationService;
import com.mownika.jobportal.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/jobseeker")
public class JobSeekerController {

    private final JobService jobService;
    private final ApplicationService applicationService;


    public JobSeekerController(JobService jobService,
                               ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @GetMapping("/jobs")
    public String viewAllJobs(Model model) {

        List<Job> jobs = jobService.getAllJobs();
        model.addAttribute("jobs", jobs);

        return "jobseeker-jobs";
    }

    @GetMapping("/job/{id}")
    public String viewJob(@PathVariable Long id,
                          Model model) {

        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);

        return "jobseeker-view-job";
    }

    @GetMapping("/job/apply/{id}")
    public String applyJob(@PathVariable Long id,
                           Principal principal) {

        String email = principal.getName();
        applicationService.applyJob(id, email);

        return "redirect:/jobseeker/jobs";
    }

    @GetMapping("/my-applications")
    public String myApplications(Model model,
                                 Principal principal) {

        String email = principal.getName();
        model.addAttribute("applications",
                applicationService.getMyApplications(email));

        return "my-applications";
    }
}