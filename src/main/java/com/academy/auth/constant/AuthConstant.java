package com.academy.auth.constant;

import java.time.Instant;

public interface AuthConstant {

   String TOKEN = "token";
   String EMAIL_VERIFICATION_URL = "email_verification_url";
   String EMAIL_RESEND_URL = "email_resend_url";
   String LOGIN_URL = "loginUrl";
   String USERNAME = "username";
   String APP_NAME = "Authentication Service";
   String USER_EMAIL_VERIFICATION_TEMPLATE_ID = "user.email.verification";
   String USER_EMAIL_VERIFICATION_SUCCESS_TEMPLATE_ID = "user.email.verification.success";
   String USER_EMAIL_VERIFICATION_FAILURE_TEMPLATE_ID = "user.email.verification.failed";
   String USER_EMAIL_VERIFICATION_SUBJECT = "Email Verification";
   String USER_EMAIL_VERIFICATION_SUCCESS_SUBJECT = "Email Verification Successfully";

   long ACCESS_TOKEN_EXP = (System.currentTimeMillis() + 1000 * 60 * 60 * 10);
   long REFRESH_TOKEN_EXP = (System.currentTimeMillis() + 1000 * 60 * 60 * 24);
   long VERIFICATION_TOKEN_EXP = (Instant.now().getEpochSecond() + 1000 * 60 * 60 * 24 * 7);

   String REDIS_HOST_ID = "redis.host";
   String REDIS_PORT_ID = "redis.port";
   String REDIS_USER_WRITE_NAME = "redis.user.write.name";
   String REDIS_USER_WRITE_PASSWORD = "redis.user.write.password";
}
