package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.CompanyDto;
import com.mownika.jobportal.entity.Company;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.exception.BadRequestException;
import com.mownika.jobportal.exception.ResourceNotFoundException;
import com.mownika.jobportal.repository.CompanyRepository;
import com.mownika.jobportal.repository.RecruiterProfileRepository;
import com.mownika.jobportal.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecruiterProfileRepository recruiterProfileRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private User user;
    private RecruiterProfile recruiter;
    private CompanyDto dto;

    private final String email = "recruiter@example.com";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(email);

        recruiter = new RecruiterProfile();
        recruiter.setUser(user);

        dto = new CompanyDto();
        dto.setName("Tech Solutions");
        dto.setAbout("Software company");
        dto.setWebsite("https://example.com");
        dto.setIndustry("Technology");
    }

    @Test
    void createCompany_success_savesCompanyAndRecruiter() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));
        when(companyRepository.existsByName("Tech Solutions"))
                .thenReturn(false);

        companyService.createCompany(dto, email);

        ArgumentCaptor<Company> captor =
                ArgumentCaptor.forClass(Company.class);

        verify(companyRepository).save(captor.capture());

        Company saved = captor.getValue();

        assertEquals("Tech Solutions", saved.getName());
        assertEquals("Software company", saved.getAbout());
        assertEquals("https://example.com", saved.getWebsite());
        assertEquals("Technology", saved.getIndustry());
        assertSame(recruiter, saved.getOwner());
        assertSame(saved, recruiter.getCompany());

        verify(recruiterProfileRepository).save(recruiter);
    }

    @Test
    void createCompany_userMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> companyService.createCompany(dto, email));

        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void createCompany_recruiterProfileMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> companyService.createCompany(dto, email));

        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void createCompany_companyAlreadyExists_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));
        when(companyRepository.existsByName("Tech Solutions"))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> companyService.createCompany(dto, email));

        verify(companyRepository, never()).save(any(Company.class));
        verify(recruiterProfileRepository, never()).save(any());
    }

    @Test
    void getCompanyByRecruiter_success_returnsCompany() {
        Company company = new Company();
        recruiter.setCompany(company);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));

        assertSame(company, companyService.getCompanyByRecruiter(email));
    }

    @Test
    void getCompanyByRecruiter_recruiterMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> companyService.getCompanyByRecruiter(email));
    }

    @Test
    void getCompanyForEdit_success_returnsCompanyDto() {
        Company company = new Company();
        company.setName("Tech Solutions");
        company.setAbout("Software company");
        company.setWebsite("https://example.com");
        company.setIndustry("Technology");
        recruiter.setCompany(company);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));

        CompanyDto result = companyService.getCompanyForEdit(email);

        assertEquals("Tech Solutions", result.getName());
        assertEquals("Software company", result.getAbout());
        assertEquals("https://example.com", result.getWebsite());
        assertEquals("Technology", result.getIndustry());
    }

    @Test
    void updateCompany_success_updatesAndSavesCompany() {
        Company company = new Company();
        recruiter.setCompany(company);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.of(recruiter));

        companyService.updateCompany(dto, email);

        assertEquals("Tech Solutions", company.getName());
        assertEquals("Software company", company.getAbout());
        assertEquals("https://example.com", company.getWebsite());
        assertEquals("Technology", company.getIndustry());

        verify(companyRepository).save(company);
    }

    @Test
    void updateCompany_recruiterProfileMissing_throwsException() {
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(recruiterProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> companyService.updateCompany(dto, email));

        verify(companyRepository, never()).save(any(Company.class));
    }
}
