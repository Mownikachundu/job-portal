package com.mownika.jobportal.service;

import com.mownika.jobportal.dto.RegisterUserDto;
import com.mownika.jobportal.entity.User;
import com.mownika.jobportal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    }
}