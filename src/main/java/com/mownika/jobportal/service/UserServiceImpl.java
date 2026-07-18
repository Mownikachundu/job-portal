package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.RegisterUserDto;
import com.mownika.jobportal.entity.JobSeekerProfile;
import com.mownika.jobportal.entity.RecruiterProfile;
import com.mownika.jobportal.entity.Role;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.JobSeekerProfileRepository;
import com.mownika.jobportal.repository.RecruiterProfileRepository;
import com.mownika.jobportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository,
                           RecruiterProfileRepository recruiterProfileRepository,
                           JobSeekerProfileRepository jobSeekerProfileRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getUserNameByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return user.getName();
    }

    @Override
    public void registerUser(RegisterUserDto registerUserDto) {

        System.out.println("registerUser() method called");

        Optional<User> existingUser =
                userRepository.findByEmail(registerUserDto.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists.");
        }

        if (!registerUserDto.getPassword()
                .equals(registerUserDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        String encryptedPassword =
                passwordEncoder.encode(registerUserDto.getPassword());

        User user = new User();

        user.setName(registerUserDto.getName());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(encryptedPassword);
        user.setRole(registerUserDto.getRole());

        userRepository.save(user);

        if (user.getRole() == Role.RECRUITER) {

            RecruiterProfile recruiterProfile = new RecruiterProfile();

            recruiterProfile.setUser(user);

            recruiterProfileRepository.save(recruiterProfile);

        } else {

            JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();

            jobSeekerProfile.setUser(user);

            jobSeekerProfileRepository.save(jobSeekerProfile);
        }
    }
}