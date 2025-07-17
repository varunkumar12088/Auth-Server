package com.academy.auth.service;

import com.academy.auth.constant.CreationSource;

public interface RefreshTokenService {

    void save(String token, CreationSource creationSource);

    void validateToken(String token, String username);

}
