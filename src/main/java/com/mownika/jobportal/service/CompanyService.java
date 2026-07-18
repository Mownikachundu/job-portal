package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.CompanyDto;
import com.mownika.jobportal.entity.Company;

public interface CompanyService {

    void createCompany(CompanyDto companyDto, String email);
    Company getCompanyByRecruiter(String email);

}