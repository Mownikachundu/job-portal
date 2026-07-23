package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.JobDto;
import com.mownika.jobportal.entity.Job;
import com.mownika.jobportal.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/recruiter/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/create")
    public String showCreateJobForm(Model model) {

        model.addAttribute("jobDto", new JobDto());

        return "create-job";
    }

    @PostMapping("/create")
    public String createJob(@ModelAttribute JobDto jobDto,
                            Principal principal) {

        String email = principal.getName();
        jobService.createJob(jobDto, email);

        return "redirect:/recruiter/job/view";
    }

    @GetMapping("/view")
    public String viewJobs(Model model,
                           Principal principal) {

        String email = principal.getName();
        List<Job> jobs = jobService.getCompanyJobs(email);
        model.addAttribute("jobs", jobs);

        return "view-jobs";
    }

    @GetMapping("/view/{id}")
    public String viewJob(@PathVariable Long id,
                          Model model) {

        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);

        return "view-job";
    }

    @GetMapping("/edit/{id}")
    public String showEditJobForm(@PathVariable Long id,
                                  Model model) {

        JobDto jobDto = jobService.getJobForEdit(id);

        model.addAttribute("jobDto", jobDto);
        model.addAttribute("id", id);

        return "edit-job";
    }

    @PostMapping("/edit/{id}")
    public String updateJob(@PathVariable Long id,
                            @ModelAttribute JobDto jobDto) {

        jobService.updateJob(id, jobDto);

        return "redirect:/recruiter/job/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {

        jobService.deleteJob(id);

        return "redirect:/recruiter/job/view";
    }
}