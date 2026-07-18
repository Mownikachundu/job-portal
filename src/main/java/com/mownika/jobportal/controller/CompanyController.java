package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.CompanyDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/recruiter/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/create")
    public String showCreateCompanyForm(Model model) {

        model.addAttribute("companyDto", new CompanyDto());
        return "create-company";
    }

    @PostMapping("/create")
    public String createCompany(@ModelAttribute CompanyDto companyDto,
                                Principal principal) {

        String email = principal.getName();
        companyService.createCompany(companyDto, email);
        return "redirect:/recruiter/dashboard";
    }

    @GetMapping("/view")
    public String viewCompany(Model model,
                              Principal principal) {

        String email = principal.getName();

        Company company = companyService.getCompanyByRecruiter(email);

        model.addAttribute("company", company);

        return "view-company";
    }

}