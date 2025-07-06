package com.learning.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final KeyPair keyPair;

    private static final String KEY_ALGORITHM = "RSA";
    private static final String API_ROLE = "Api-Role";
    private static final String SUB = "sub";
    private static final String IAT = "iat";
    private static final String EXP = "exp";
    private static final String SEED = "csjbzhdfhfscnzbbdsjbhfhsgdaLKjfshlahkgsfakdghfhksdvbshdbhsdabgkhsahkfhasfahhfshjs";

    public JwtUtil() throws Exception {
        this.keyPair = generateKeyPair(SEED);
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10));
    }

    public String generateRefreshToken(UserDetails userDetails) {
       return buildToken(userDetails, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10 * 2));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parse(token)
                    .getPayload();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractUserRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get(API_ROLE, String.class);
    }

    private KeyPair generateKeyPair(String seed) throws Exception {
        SecureRandom secureRandom = new SecureRandom(seed.getBytes());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    private String buildToken(UserDetails userDetails, Date expiration) {
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        Map<String, String> claims = Map.of(
                "username", userDetails.getUsername(),
                "role", role
        );
        return Jwts.builder()
                .claims(claims)
                .claim(SUB, userDetails.getUsername())
                .claim(IAT, new Date())
                .claim(EXP, expiration)
                .signWith(privateKey)
                .compact();
    }

}
