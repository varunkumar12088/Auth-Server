package com.academy.auth.service.impl;

import com.academy.auth.entity.User;
import com.academy.auth.service.ValidationService;
import com.academy.auth.dto.LoginRequest;
import com.academy.auth.dto.RegistrationRequest;
import com.academy.auth.repository.UserRepository;
import com.academy.common.constant.UserRole;
import com.academy.common.util.ValidationUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValidEmail(String email) {
        return ValidationUtil.isEmailValid(email);
    }

    @Override
    public boolean isValidPassword(String password) {
        return ValidationUtil.isPasswordValid(password);

    }

    @Override
    public void validateRole(String role) throws IllegalArgumentException {
        if (StringUtils.isBlank(role)) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        if (!UserRole.isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public boolean isUsernamePresent(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        User user =  userRepository.findByUsernameOrEmail(username, username);
        if (ObjectUtils.isNotEmpty(user)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmailPresent(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        User user = userRepository.findByUsernameOrEmail(email, email);
        if (ObjectUtils.isNotEmpty(user)) {
            return true;
        }
        return false;
    }

    @Override
    public void validateRegistrationRequest(RegistrationRequest request) throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }
        if (StringUtils.isBlank(request.getEmail()) || !isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (StringUtils.isBlank(request.getUsername())) {
            request.setUsername(request.getEmail());
        }
        if (StringUtils.isBlank(request.getPassword()) || !isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        if (StringUtils.isBlank(request.getRole())) {
            request.setRole("USER");
        } else {
            validateRole(request.getRole());
        }
        if (isUsernamePresent(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if(isEmailPresent(request.getEmail()) ) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    @Override
    public void validateLoginRequest(LoginRequest loginRequest) throws IllegalArgumentException {
        if (loginRequest == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }
        if (StringUtils.isBlank(loginRequest.getUsername())) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (StringUtils.isBlank(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (!isValidPassword(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit");
        }

    }
}
