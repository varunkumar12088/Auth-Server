package com.academy.auth.utils;

import com.academy.auth.constant.AuthConstant;
import com.academy.auth.entity.User;
import com.academy.common.constant.CommonConstant;
import com.academy.common.entity.ConfigVar;
import com.academy.common.service.ConfigVarService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private KeyPair keyPair;

    private static final String KEY_ALGORITHM = "RSA";

    private final ConfigVarService<ConfigVar, String> configVarService;

    public JwtUtil(ConfigVarService<ConfigVar, String> configVarService) {
        this.configVarService = configVarService;
    }


    @PostConstruct
    public void init(){
        final String seed = configVarService.getConfigVarValue(CommonConstant.ENCRYPTION_KEY_ID, String.class, CommonConstant.ENCRYPTION_KEY);
        this.keyPair = generateKeyPair(seed);
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
    public String generateToken(UserDetails userDetails, String jti, String appName, long exp) {
        return buildToken(userDetails, jti, appName, exp);
    }

    public String generateVerificationToken(User user) {
        return buildToken(user, new Date(AuthConstant.VERIFICATION_TOKEN_EXP));
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
        return claims.get(AuthConstant.USER_ROLES, String.class);
    }

    public String extractAppName(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get(AuthConstant.APP_NAME, String.class);
    }

    public String extractJTI(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get(AuthConstant.JTI, String.class);
    }

    private KeyPair generateKeyPair(String seed) {
        try {
            SecureRandom secureRandom = new SecureRandom(seed.getBytes());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(2048, secureRandom);
            return keyPairGenerator.generateKeyPair();
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private String buildToken(UserDetails userDetails, String jti, String appName, long expiration) {
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        Map<String, Object> claims = Map.of(
                AuthConstant.USERNAME, userDetails.getUsername(),
                AuthConstant.USER_ROLES, role,
                AuthConstant.SUB, userDetails.getUsername(),
                AuthConstant.IAT, new Date(),
                AuthConstant.EXP, new Date(expiration),
                AuthConstant.JTI, jti,
                AuthConstant.APP_NAME, appName
        );
        return Jwts.builder()
                .claims(claims)
                .signWith(privateKey)
                .compact();
    }

    private String buildToken(User user, Date expiration) {
        String role = user.getRole();

        Map<String, String> claims = Map.of(
                AuthConstant.USERNAME, user.getEmail(),
                AuthConstant.USER_ROLES, role
        );
        return Jwts.builder()
                .claims(claims)
                .claim(AuthConstant.SUB, user.getEmail())
                .claim(AuthConstant.IAT, new Date())
                .claim(AuthConstant.EXP, expiration)
                .signWith(privateKey)
                .compact();
    }
}
