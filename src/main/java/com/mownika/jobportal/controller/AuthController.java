package com.mownika.jobportal.controller;

import com.mownika.jobportal.dto.RegisterUserDto;
import com.mownika.jobportal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("registerUserDto", new RegisterUserDto());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute RegisterUserDto dto,
            Model model) {

        try {

            userService.registerUser(dto);

            return "redirect:/login";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());

            return "register";
        }

    }
}