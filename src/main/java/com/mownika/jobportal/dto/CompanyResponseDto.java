package com.mownika.jobportal.dto;

import java.time.LocalDateTime;

public class CompanyResponseDto {

    private Long id;
    private String name;
    private String about;
    private String website;
    private String industry;
    private LocalDateTime createdAt;

    public CompanyResponseDto() {
    }

    public CompanyResponseDto(Long id, String name, String about,
                              String website, String industry,
                              LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.website = website;
        this.industry = industry;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}