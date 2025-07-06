package com.learning.auth.service;

import com.learning.auth.dto.LoginRequest;
import com.learning.auth.dto.AuthResponse;
import com.learning.auth.dto.RefreshTokenRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);


    void logout(String token);
}
