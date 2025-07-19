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
    private static final String SUB = "sub";
    private static final String IAT = "iat";
    private static final String EXP = "exp";
    private static final String JTI = "jti";
    private static final String APP_NAME  = "appName";
    private static final String USERNAME = "username";
    private static final String USER_ROLES = "roles";

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
        return claims.get(USER_ROLES, String.class);
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
                USERNAME, userDetails.getUsername(),
                USER_ROLES, role,
                SUB, userDetails.getUsername(),
                IAT, new Date(),
                EXP, new Date(expiration),
                JTI, jti,
                APP_NAME, appName
        );
        return Jwts.builder()
                .claims(claims)
                .signWith(privateKey)
                .compact();
    }

    private String buildToken(User user, Date expiration) {
        String role = user.getRole();

        Map<String, String> claims = Map.of(
                "username", user.getEmail(),
                "role", role
        );
        return Jwts.builder()
                .claims(claims)
                .claim(SUB, user.getEmail())
                .claim(IAT, new Date())
                .claim(EXP, expiration)
                .signWith(privateKey)
                .compact();
    }
}
