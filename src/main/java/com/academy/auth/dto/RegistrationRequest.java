package com.academy.auth.dto;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;
    private String password;
    private String email;
    private String role;
    private boolean enabled;

}
