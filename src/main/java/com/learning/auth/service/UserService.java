package com.learning.auth.service;

import com.learning.auth.dto.RegistrationRequest;
import com.learning.auth.dto.UpdatePassword;

public interface UserService {

     void registerUser(RegistrationRequest request);

     void changePassword(UpdatePassword updatePassword);

    void resetPassword(String email, String newPassword);

    boolean isUserExists(String username);

    boolean isEmailExists(String email);

    String getUserRole(String username);

    String getUserId(String username);

    void updateUserDetails(String username, RegistrationRequest request);

    void deleteUser(String username);

    void enableUser(String username);

    void disableUser(String username);

}
