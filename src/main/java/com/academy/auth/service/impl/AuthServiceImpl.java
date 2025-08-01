package com.academy.auth.service.impl;

import com.academy.auth.constant.AppName;
import com.academy.auth.constant.AuthConstant;
import com.academy.auth.constant.EventType;
import com.academy.auth.service.AuthService;
import com.academy.auth.service.ValidationService;
import com.academy.auth.dto.LoginRequest;
import com.academy.auth.dto.AuthResponse;
import com.academy.auth.dto.RefreshTokenRequest;
import com.academy.auth.exception.UnAuthException;
import com.academy.auth.utils.JwtUtil;
import com.academy.common.event.bus.BufferedEventBus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String COLON = ":";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ValidationService validationService;

    @Autowired
    private BufferedEventBus bufferedEventBus;

    @Override
    public AuthResponse login(LoginRequest request) {
        LOGGER.info("Processing login request for user: {}", request.getUsername());
        validationService.validateLoginRequest(request);
        try{
            if(!AppName.isValidAppName(request.getAppName())){
                request.setAppName(AppName.DEFAULT.name());
            }
            String username = request.getAppName() + COLON + request.getUsername();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            LOGGER.debug("Authentication attempt for user: {}", request.getUsername());

            // Check if the authentication was successful
            if (authentication.isAuthenticated()) {
                LOGGER.info("User {} authenticated successfully", request.getUsername());
                // Load user details to generate JWT tokens
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

                // Generate JWT tokens
                String jti = UUID.randomUUID().toString();
                String accessToken = generateAccessToken(userDetails, request.getAppName(), jti);
                String refreshToken = generateRefreshToken(userDetails, request.getAppName(), jti);

                LOGGER.info("Generated JWT token for user: {}", request.getUsername());
                 return new AuthResponse(accessToken, refreshToken);
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
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        LOGGER.info("Processing refresh token request");
        // Validate the refresh token
        if(!jwtUtil.validateToken(request.getRefreshToken())){
            throw new UnAuthException("Invalid refresh token");
        }

        // Extract the username from the refresh token
        String username = jwtUtil.extractUsername(request.getRefreshToken());
        if(StringUtils.isBlank(username)) {
            throw new UnAuthException("Invalid refresh token: no username found");
        }
        String appName = jwtUtil.extractAppName(request.getRefreshToken());

        String finalUsername = appName + COLON + username;

        UserDetails userDetails = userDetailsService.loadUserByUsername(finalUsername);

        // Generate JWT tokens
        String jti = UUID.randomUUID().toString();
        String accessToken = generateAccessToken(userDetails, appName, jti);
        String refreshToken = generateRefreshToken(userDetails, appName, jti);
        //Remove the token from
        removeTokenFromCache(request.getRefreshToken(), jti);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public void logout(String token) {
        LOGGER.debug("logout ");
        removeTokenFromCache(token, "");
    }

    private String generateAccessToken(UserDetails userDetails, String appName, String jti){
        long exp = AuthConstant.ACCESS_TOKEN_EXP;
        Map<String, Object> eventData = getEventData(userDetails.getUsername(), appName, jti, exp);
        bufferedEventBus.publish(EventType.ACCESS_TOKEN_UPDATE.name(), eventData);
        return jwtUtil.generateToken(userDetails, jti, appName, exp);
    }

    private String generateRefreshToken(UserDetails userDetails, String appName, String jti){
        long exp = AuthConstant.ACCESS_TOKEN_EXP;
        Map<String, Object> eventData = getEventData(userDetails.getUsername(), appName, jti, exp);
        bufferedEventBus.publish(EventType.REFRESH_TOKEN_UPDATE.name(), eventData);
        return jwtUtil.generateToken(userDetails, jti, appName, exp);
    }

    private Map<String, Object> getEventData(String username, String appName, String jti, long exp){
        Map<String, Object> eventData = new HashMap<>();
        eventData.put(AuthConstant.APP_NAME, appName);
        eventData.put(AuthConstant.USERNAME, username);
        eventData.put(AuthConstant.JTI, jti);
        eventData.put(AuthConstant.EXP, exp);
        return eventData;
    }

    private void removeTokenFromCache(String token, String by){
        String username = jwtUtil.extractUsername(token);
        String appName = jwtUtil.extractAppName(token);
        String jti = jwtUtil.extractJTI(token);
        Map<String, Object> eventData = getEventData(username, appName, jti, 0);
        eventData.put(AuthConstant.BY, by);
        bufferedEventBus.publish(EventType.TOKEN_REMOVE.name(), eventData);
    }
}
