package com.academy.auth.service;

import com.academy.auth.dto.LoginRequest;
import com.academy.auth.dto.AuthResponse;
import com.academy.auth.dto.RefreshTokenRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);


    void logout(String token);
}
