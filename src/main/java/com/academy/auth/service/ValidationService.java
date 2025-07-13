package com.academy.auth.service;

import com.academy.auth.dto.LoginRequest;
import com.academy.auth.dto.RegistrationRequest;

public interface ValidationService {

    boolean isValidEmail(String email);

    boolean isValidPassword(String password);
    void validateRole(String role) throws IllegalArgumentException;

    boolean isUsernamePresent(String username);
    boolean isEmailPresent(String email);

    void validateRegistrationRequest(RegistrationRequest request) throws IllegalArgumentException;

    void validateLoginRequest(LoginRequest loginRequest) throws IllegalArgumentException;



}
