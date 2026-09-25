package com.mownika.jobportal.dto;

import java.time.LocalDateTime;

public class JobResponseDto {

    private Long id;
    private String title;
    private String description;
    private Double salary;
    private String location;
    private Integer experienceRequired;
    private Long companyId;
    private String companyName;
    private LocalDateTime createdAt;

    public JobResponseDto() {
    }

    public JobResponseDto(Long id, String title, String description,
                          Double salary, String location,
                          Integer experienceRequired,
                          Long companyId, String companyName,
                          LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.location = location;
        this.experienceRequired = experienceRequired;
        this.companyId = companyId;
        this.companyName = companyName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getSalary() {
        return salary;
    }

    public String getLocation() {
        return location;
    }

    public Integer getExperienceRequired() {
        return experienceRequired;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExperienceRequired(Integer experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}