package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.CompanyDto;
import com.mownika.jobportal.dto.CompanyResponseDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/recruiter/company")
public class CompanyRestController {

    private final CompanyService companyService;

    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<String> createCompany(
            @Valid @RequestBody CompanyDto companyDto,
            Principal principal) {

        String email = principal.getName();

        companyService.createCompany(companyDto, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Company created successfully");
    }

    @GetMapping
    public ResponseEntity<CompanyResponseDto> getCompany(
            Principal principal) {

        String email = principal.getName();

        Company company =
                companyService.getCompanyByRecruiter(email);

        CompanyResponseDto response =
                new CompanyResponseDto(
                        company.getId(),
                        company.getName(),
                        company.getAbout(),
                        company.getWebsite(),
                        company.getIndustry(),
                        company.getCreatedAt()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<String> updateCompany(
            @Valid @RequestBody CompanyDto companyDto,
            Principal principal) {

        String email = principal.getName();

        companyService.updateCompany(companyDto, email);

        return ResponseEntity.ok("Company updated successfully");
    }
}