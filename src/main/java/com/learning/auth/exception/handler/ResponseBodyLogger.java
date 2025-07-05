package com.learning.auth.exception.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseBodyLogger implements ResponseBodyAdvice<Object> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBodyLogger.class);


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        LOGGER.debug("After completing Response Body: {} for API: {} and method: ", body, request.getURI(), request.getMethod());
        // Get the controller class name
        String controllerClass = returnType.getDeclaringClass().getName();
        // Method name
        String methodName = returnType.getMethod().getName();
        String fullMethodName = controllerClass + "." + methodName;
        try {

            String jsonStr = OBJECT_MAPPER.writeValueAsString(body);
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
            if (jsonNode.has("password")) {
                ((ObjectNode) body).put("password", "******");
            }
            LOGGER.debug("After returning controller: {}, request body: {}", fullMethodName, jsonNode.toPrettyString());
        } catch (Exception e) {
            LOGGER.debug("After returning controller: {}, request body: {} ", fullMethodName, body);
        }
        return body;
    }
}
