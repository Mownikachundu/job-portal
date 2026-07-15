package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.CompanyDto;
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

    }
}