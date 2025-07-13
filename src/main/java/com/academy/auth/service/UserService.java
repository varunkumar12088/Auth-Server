package com.academy.auth.service;

import com.academy.auth.dto.RegistrationRequest;
import com.academy.auth.dto.UpdatePassword;

public interface UserService {

     void registerUser(RegistrationRequest request);

     void changePassword(UpdatePassword updatePassword);

    void resetPassword(String email, String newPassword);

    boolean isUserExists(String username);

    boolean isEmailExists(String email);

    void updateUserDetails(String username, RegistrationRequest request);

    void deleteUser(String username);

    void disableUser(String username);

    String verifyEmail(String email, String token);

    String resentVerificationEmail(String email);

}
