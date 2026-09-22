package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.LoginDto;
import com.mownika.jobportal.dto.RegisterUserDto;
import com.mownika.jobportal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthRestController(UserService userService,
                              AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterUserDto dto) {

        userService.registerUser(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        return ResponseEntity.ok("Login successful");
    }
}