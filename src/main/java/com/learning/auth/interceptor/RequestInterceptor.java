package com.learning.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final String X_TRACKING_ID = "X-Tracking-Id";
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String trackingId = request.getHeader(X_TRACKING_ID);
        if(StringUtils.isBlank(trackingId)){
            trackingId = UUID.randomUUID().toString();
        }
        MDC.put(X_TRACKING_ID, trackingId);
        LOGGER.debug("Injected header {} with value {} into the request", X_TRACKING_ID, trackingId);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();
        if (StringUtils.isNotBlank(query)) {
            uri += "?" + query;
        }

        LOGGER.debug("Before controller Request URI: {}, method: {}", uri, method);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        if (ex != null) {
            LOGGER.error("Request processing failed for URI: {}, method: {}, duration: {} ms, error: {}", request.getRequestURI(), request.getMethod(), duration, ex);
        } else {
            LOGGER.debug("Request processing completed for URI: {}, method: {}, duration: {} ms", request.getRequestURI(), request.getMethod(), duration);
        }
        MDC.clear();
    }

}
