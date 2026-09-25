package com.mownika.jobportal.dto;

import com.mownika.jobportal.entity.ApplicationStatus;

import java.time.LocalDateTime;

public class ApplicationResponseDto {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long userId;
    private String userName;
    private String userEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;

    public ApplicationResponseDto() {
    }

    public ApplicationResponseDto(
            Long id,
            Long jobId,
            String jobTitle,
            Long userId,
            String userName,
            String userEmail,
            ApplicationStatus status,
            LocalDateTime appliedDate) {

        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}