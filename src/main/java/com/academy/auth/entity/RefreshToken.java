package com.academy.auth.entity;

import com.academy.auth.constant.CreationSource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collation = "refresh_token")
public class RefreshToken {

    @Id
    private String id;
    @Indexed
    private String username;
    @Indexed(unique = true)
    private String token;

    private Instant issuedAt;
    private Instant expiresAt;

    private String ipAddress;
    private String userAgent;

    private boolean revoked;
    private String replacedByToken;

    private CreationSource creationSource;
    private String createdDTM;
    private String updateDTM;
}
