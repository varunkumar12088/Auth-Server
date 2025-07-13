package com.academy.auth.client;

import com.academy.auth.constant.AuthConstant;
import com.academy.auth.dto.EmailDTO;
import com.academy.auth.entity.User;
import com.academy.auth.utils.JwtUtil;
import com.academy.common.client.AbstractRestClient;
import com.academy.common.constant.CommonConstant;
import com.academy.common.constant.UserRole;
import com.academy.common.service.impl.ConfigVarServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailClient extends AbstractRestClient {

     private static final Logger logger = LoggerFactory.getLogger(EmailClient.class);

     @Autowired
     private ConfigVarServiceImpl configVarService;

     @Autowired
     private JwtUtil jwtUtil;

    @Override
    public String getBaseUrl() {
        String baseUrl = configVarService.getConfigVar("email.base.url", String.class, "http://localhost:8085");
        return baseUrl;
    }

    public void sendEmailVerification(User user){
        String uri = configVarService.getConfigVar("email.send.uri", String.class, "/api/v1/emails/send");
        logger.info("Sending email verification request to URI: {}", uri);
        String verificationToken = jwtUtil.generateVerificationToken(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put(AuthConstant.USERNAME, user.getUsername());
        variables.put(AuthConstant.TOKEN, verificationToken);
        variables.put(AuthConstant.EMAIL_VERIFICATION_URL, "http://localhost:8081/api/v1/auth/users/verify-email");

        EmailDTO emailDTO = EmailDTO.builder().to(user.getEmail())
                .subject(AuthConstant.USER_EMAIL_VERIFICATION_SUBJECT)
                .applicationName(AuthConstant.APP_NAME)
                .templateId(AuthConstant.USER_EMAIL_VERIFICATION_TEMPLATE_ID)
                .build();

        Map<String, String> headers = new HashMap<>();
        headers.put(CommonConstant.X_USER_EMAIL, user.getEmail());
        headers.put(CommonConstant.ROLE_HEADER, UserRole.INTERNAL.name());

        this.post(uri, String.class, emailDTO, null, headers);
    }
}
