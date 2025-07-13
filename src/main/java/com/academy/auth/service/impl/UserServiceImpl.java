package com.academy.auth.service.impl;

import com.academy.auth.constant.EventType;
import com.academy.auth.entity.User;
import com.academy.auth.service.UserService;
import com.academy.auth.service.ValidationService;
import com.academy.auth.dto.RegistrationRequest;
import com.academy.auth.dto.UpdatePassword;
import com.academy.auth.exception.UnAuthException;
import com.academy.auth.exception.UserNotFoundException;
import com.academy.auth.repository.UserRepository;
import com.academy.auth.utils.JwtUtil;
import com.academy.common.event.bus.BufferedEventBus;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BufferedEventBus bufferedEventBus;

    @Override
    public void registerUser(RegistrationRequest request) {
        LOGGER.info("Registering user with username: {}", request.getUsername());

        validationService.validateRegistrationRequest(request);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(encodedPassword);
        user.setEnabled(false);
        userRepository.save(user);
        bufferedEventBus.publish(EventType.EMAIL_VERIFICATION.name(), user);
    }

    @Override
    public void changePassword(UpdatePassword updatePassword) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(updatePassword.getUsername(), updatePassword.getOldPassword()));
        if (authentication.isAuthenticated()) {
            boolean isValid = validationService.isValidPassword(updatePassword.getNewPassword());
            if(!isValid) {
                throw new IllegalArgumentException("New password is not valid");
            }

            User user = userRepository.findByUsernameOrEmail(updatePassword.getUsername(), updatePassword.getUsername());
            if (user != null) {
                String encodedNewPassword = passwordEncoder.encode(updatePassword.getNewPassword());
                user.setPassword(encodedNewPassword);
                userRepository.save(user);
                LOGGER.info("Password changed successfully for user: {}", updatePassword.getUsername());
                return;
            }
            throw new UserNotFoundException("User not found");
        }
        throw new UnAuthException("Old password is wrong");
    }

    @Override
    public String verifyEmail(String email, String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                User user = userRepository.findByUsernameOrEmail(username, username);
                if (ObjectUtils.isNotEmpty(user)) {
                    user.setEnabled(true);
                    userRepository.save(user);
                }
            }
            LOGGER.info("Email verification successful for user: {}", email);
            bufferedEventBus.publish(EventType.EMAIL_VERIFICATION_SUCCESS.name(), email);
            return "Email verification successful for user: " + email;
        } catch (Exception ex) {
            LOGGER.error("Email verification failed for token: {}", token, ex);
            bufferedEventBus.publish(EventType.EMAIL_VERIFICATION_FAILURE.name(), email);
            return "Email verification failure for user: " + email;
        }
    }

    @Override
    public String resentVerificationEmail(String email) {
        LOGGER.debug("Resending verification email for user: {}", email);
        User user = userRepository.findByUsernameOrEmail(email, email);
        bufferedEventBus.publish(EventType.EMAIL_VERIFICATION.name(), user);
        return "Verification email resent to: " + email + ". Please check your inbox.";
    }

    @Override
    public void resetPassword(String email, String newPassword) {

    }

    @Override
    public boolean isUserExists(String username) {
        return false;
    }

    @Override
    public boolean isEmailExists(String email) {
        return false;
    }


    @Override
    public void updateUserDetails(String username, RegistrationRequest request) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void disableUser(String username) {

    }

}
