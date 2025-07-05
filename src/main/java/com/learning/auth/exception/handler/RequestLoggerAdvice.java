package com.learning.auth.exception.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@ControllerAdvice
public class RequestLoggerAdvice extends RequestBodyAdviceAdapter {


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggerAdvice.class);
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Get the controller class name
        String controllerClass = parameter.getDeclaringClass().getName();
        // Method name
        String methodName = parameter.getMethod().getName();
        String fullMethodName = controllerClass + "." + methodName;
        try {
            String jsonStr = OBJECT_MAPPER.writeValueAsString(body);
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
            if (jsonNode.has("password")) {
                ((ObjectNode) jsonNode).put("password", "******");
            }
            LOGGER.debug("Before controller: {}, request body: {}", fullMethodName, jsonNode.toPrettyString());
        } catch (Exception e) {
            LOGGER.debug("Before controller: {}, request body: {} ", fullMethodName, body);
        }
        return body;
    }
}

