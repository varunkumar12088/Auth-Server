package com.learning.auth.config;

import com.learning.auth.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    public InterceptorConfig(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the RequestInterceptor
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**");
    }
}
