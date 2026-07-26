package com.mownika.jobportal.controller;

import com.mownika.jobportal.entity.Application;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.service.ApplicationService;
import com.mownika.jobportal.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/job/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId,
                              Principal principal) {

        String email = principal.getName();

        applicationService.applyForJob(jobId, email);

        return "redirect:/jobseeker/jobs";
    }

    @GetMapping("/applications")
    public String myApplications(Model model,
                                 Principal principal) {

        String email = principal.getName();
        List<Application> applications = applicationService.getMyApplications(email);
        model.addAttribute("applications", applications);

        return "my-applications";
    }

    @GetMapping("/applicants/{jobId}")
    public String viewApplicants(@PathVariable Long jobId,
                                 Model model) {

        List<Application> applications =
                applicationService.getApplicants(jobId);

        model.addAttribute("applications", applications);

        return "view-applicants";
    }
}