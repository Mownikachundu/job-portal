package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.CompanyDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.CompanyRepository;
import com.mownika.jobportal.repository.RecruiterProfileRepository;
import com.mownika.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              UserRepository userRepository,
                              RecruiterProfileRepository recruiterProfileRepository) {

        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public void createCompany(CompanyDto companyDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RecruiterProfile recruiter =
                recruiterProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter profile not found"));

        if (companyRepository.existsByName(companyDto.getName())) {
            throw new RuntimeException("Company already exists");
        }

        Company company = new Company();

        company.setName(companyDto.getName());
        company.setAbout(companyDto.getAbout());
        company.setWebsite(companyDto.getWebsite());
        company.setIndustry(companyDto.getIndustry());

        company.setOwner(recruiter);
        companyRepository.save(company);

        recruiter.setCompany(company);
        recruiterProfileRepository.save(recruiter);
    }

    @Override
    public Company getCompanyByRecruiter(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter profile not found"));

        return recruiterProfile.getCompany();
    }

    @Override
    public CompanyDto getCompanyForEdit(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter profile not found"));

        Company company = recruiterProfile.getCompany();

        CompanyDto companyDto = new CompanyDto();

        companyDto.setName(company.getName());
        companyDto.setAbout(company.getAbout());
        companyDto.setWebsite(company.getWebsite());
        companyDto.setIndustry(company.getIndustry());

        return companyDto;
    }

    @Override
    public void updateCompany(CompanyDto companyDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RecruiterProfile recruiterProfile =
                recruiterProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter profile not found"));

        Company company = recruiterProfile.getCompany();

        company.setName(companyDto.getName());
        company.setAbout(companyDto.getAbout());
        company.setWebsite(companyDto.getWebsite());
        company.setIndustry(companyDto.getIndustry());

        companyRepository.save(company);
    }
}