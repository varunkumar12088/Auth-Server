package com.learning.auth.service.impl;

import com.learning.auth.dto.LoginRequest;
import com.learning.auth.dto.LoginResponse;
import com.learning.auth.exception.UnAuthException;
import com.learning.auth.repository.UserRepository;
import com.learning.auth.service.AuthService;
import com.learning.auth.service.ValidationService;
import com.learning.auth.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ValidationService validationService;

    @Override
    public LoginResponse login(LoginRequest request) {
        LOGGER.info("Processing login request for user: {}", request.getUsername());
        validationService.validateLoginRequest(request);
        try{

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
            if (authentication.isAuthenticated()) {
                LOGGER.info("User {} authenticated successfully", request.getUsername());

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                String role = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("USER");
                Map<String, String> claims = Map.of(
                        "username", request.getUsername(),
                        "role", role
                );
                String token = jwtUtil.generateToken(request.getUsername(), claims);
                LOGGER.info("Generated JWT token for user: {}", request.getUsername());
                 return new LoginResponse(token);
            } else {
                LOGGER.warn("Authentication failed for user: {}", request.getUsername());
               throw new UnAuthException("Username or password wrong");
            }
        } catch (Exception ex){
            LOGGER.error("Login failed for user: {}", request.getUsername(), ex);
            throw new UnAuthException("Username or password wrong");
        }
    }

    @Override
    public void logout(String token) {

    }
}
