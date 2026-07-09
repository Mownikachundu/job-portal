package com.mownika.jobportal.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("JOB_SEEKER")) {

            response.sendRedirect("/jobseeker/dashboard");

        } else {

            response.sendRedirect("/recruiter/dashboard");

        }

    }
}