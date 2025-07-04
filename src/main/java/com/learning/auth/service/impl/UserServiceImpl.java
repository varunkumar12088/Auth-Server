package com.learning.auth.service.impl;

import com.learning.auth.dto.RegistrationRequest;
import com.learning.auth.dto.UpdatePassword;
import com.learning.auth.entity.User;
import com.learning.auth.exception.UnAuthException;
import com.learning.auth.exception.UserNotFoundException;
import com.learning.auth.repository.UserRepository;
import com.learning.auth.service.UserService;
import com.learning.auth.service.ValidationService;
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
    public String getUserRole(String username) {
        return null;
    }

    @Override
    public String getUserId(String username) {
        return null;
    }

    @Override
    public void updateUserDetails(String username, RegistrationRequest request) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void enableUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
            LOGGER.info("User {} enabled successfully", username);
        } else {
            throw new IllegalArgumentException("User not found with username or email: " + username);
        }
    }

    @Override
    public void disableUser(String username) {

    }
}
