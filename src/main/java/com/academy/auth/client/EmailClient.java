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

        String verificationUrl = String.format("http://localhost:8081/api/v1/auth/users/%s/verify-email", user.getEmail());
        logger.info("Generated verification url: {}", verificationUrl);
        Map<String, Object> variables = new HashMap<>();
        variables.put(AuthConstant.USERNAME, user.getUsername());
        variables.put(AuthConstant.TOKEN, verificationToken);
        variables.put(AuthConstant.EMAIL_VERIFICATION_URL, verificationUrl);

        EmailDTO emailDTO = getEmailDTO(user.getEmail(), AuthConstant.USER_EMAIL_VERIFICATION_SUBJECT, AuthConstant.USER_EMAIL_VERIFICATION_TEMPLATE_ID, variables);

        Map<String, String> headers = getHeaders(user.getEmail());
        this.post(uri, String.class, emailDTO, null, headers);
    }

    public void sendEmailVerificationSuccess(String email){
        String uri = configVarService.getConfigVar("email.send.uri", String.class, "/api/v1/emails/send");
        logger.info("Sending email verification failed to URI: {}", uri);

        Map<String, Object> variables = new HashMap<>();
        variables.put(AuthConstant.LOGIN_URL, "http://localhost:8080/api/v1/auth/login");

        EmailDTO emailDTO = getEmailDTO(email, AuthConstant.USER_EMAIL_VERIFICATION_SUCCESS_SUBJECT, AuthConstant.USER_EMAIL_VERIFICATION_SUCCESS_TEMPLATE_ID, variables);

        Map<String, String> headers = getHeaders(email);
        this.post(uri, String.class, emailDTO, null, headers);
    }

    public void sendEmailVerificationFailure(String email){
        String uri = configVarService.getConfigVar("email.send.uri", String.class, "/api/v1/emails/send");
        logger.info("Sending email verification failed to URI: {}", uri);

        String resendVerificationEmailUrl = String.format("http://localhost:8080/api/v1/auth/users/%s/resend-verification-email", email);
        Map<String, Object> variables = new HashMap<>();
        variables.put(AuthConstant.EMAIL_RESEND_URL, resendVerificationEmailUrl);

        EmailDTO emailDTO = getEmailDTO(email, AuthConstant.USER_EMAIL_VERIFICATION_SUCCESS_SUBJECT, AuthConstant.USER_EMAIL_VERIFICATION_SUCCESS_TEMPLATE_ID, variables);

        Map<String, String> headers = getHeaders(email);
        this.post(uri, String.class, emailDTO, null, headers);
    }
    private EmailDTO getEmailDTO(String email, String subject, String templateId, Map<String, Object> variables) {
        return EmailDTO.builder()
                .to(email)
                .subject(subject)
                .applicationName(AuthConstant.APP_NAME)
                .templateVariables(variables)
                .templateId(templateId)
                .build();
    }

    private Map<String, String> getHeaders(String email) {
        Map<String, String> headers = new HashMap<>();
        headers.put(CommonConstant.X_USER_EMAIL, email);
        headers.put(CommonConstant.ROLE_HEADER, UserRole.INTERNAL.name());
        return headers;
    }
}
