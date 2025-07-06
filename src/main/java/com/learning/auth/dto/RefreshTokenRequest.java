package com.learning.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RefreshTokenRequest {

    private String refreshToken;
    private String accessToken;
}
