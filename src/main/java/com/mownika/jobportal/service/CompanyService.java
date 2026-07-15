package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.CompanyDto;

public interface CompanyService {

    void createCompany(CompanyDto companyDto, String email);

}