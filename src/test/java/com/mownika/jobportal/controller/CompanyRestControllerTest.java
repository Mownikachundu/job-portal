
package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.CompanyDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.service.CompanyService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyRestControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyRestController companyController;

    private Principal principal() {
        return () -> "recruiter@example.com";
    }

    @Test
    void createCompany_success_returnsCreated() {
        CompanyDto dto = new CompanyDto();

        ResponseEntity<String> response =
                companyController.createCompany(dto, principal());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Company created successfully", response.getBody());

        verify(companyService)
                .createCompany(dto, "recruiter@example.com");
    }

    @Test
    void getCompany_success_returnsCompanyResponse() {
        Company company = new Company();
        company.setName("Tech Solutions");
        company.setAbout("Software company");
        company.setWebsite("https://example.com");
        company.setIndustry("Technology");

        when(companyService.getCompanyByRecruiter("recruiter@example.com"))
                .thenReturn(company);

        ResponseEntity<?> response =
                companyController.getCompany(principal());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(companyService)
                .getCompanyByRecruiter("recruiter@example.com");
    }

    @Test
    void updateCompany_success_returnsOk() {
        CompanyDto dto = new CompanyDto();

        ResponseEntity<String> response =
                companyController.updateCompany(dto, principal());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Company updated successfully", response.getBody());

        verify(companyService)
                .updateCompany(dto, "recruiter@example.com");
    }
}
