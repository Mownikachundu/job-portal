package com.mownika.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_seeker_profiles")
public class JobSeekerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String phone;

    private String education;

    private String skills;

    private int experience;
}