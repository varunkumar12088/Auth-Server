package com.learning.auth.service;

import com.learning.auth.dto.LoginRequest;
import com.learning.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(String token);
}
