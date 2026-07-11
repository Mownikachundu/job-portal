package com.mownika.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyDto {

    @NotBlank(message = "Company name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "About company is required")
    @Size(max = 1000)
    private String about;

    @Size(max = 255)
    private String website;

    @NotBlank(message = "Industry is required")
    @Size(max = 100)
    private String industry;

    public CompanyDto() {
    }

    public CompanyDto(String name, String about,
                      String website, String industry) {
        this.name = name;
        this.about = about;
        this.website = website;
        this.industry = industry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
