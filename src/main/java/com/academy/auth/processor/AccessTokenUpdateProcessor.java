package com.academy.auth.processor;

import com.academy.auth.constant.AppName;
import com.academy.auth.constant.AuthConstant;
import com.academy.auth.constant.EventType;
import com.academy.auth.service.RedisService;
import com.academy.auth.utils.ObjectUtil;
import com.academy.common.domain.Event;
import com.academy.common.util.KeyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Component
public class AccessTokenUpdateProcessor extends AbstractProcessor<Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenUpdateProcessor.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean canProcess(Event event) {
        return this.canProcess(event, EventType.ACCESS_TOKEN_UPDATE);
    }

    @Override
    public void process(Event event) {
        LOGGER.debug("Processing event for redis access token update");
        try {
            Map<String, Object> eventData = (Map<String, Object>) event.getData();
            String appName = (String) eventData.getOrDefault(AuthConstant.APP_NAME, AppName.DEFAULT.name());
            String username = (String) eventData.getOrDefault(AuthConstant.USERNAME, "");
            String jti = (String) eventData.getOrDefault(AuthConstant.JTI, UUID.randomUUID().toString());
            String key = KeyUtil.redisTokenKey(appName, username, jti, AuthConstant.ACCESS_TOKEN);
            Long exp = (Long) eventData.getOrDefault(AuthConstant.EXP, AuthConstant.ACCESS_TOKEN_EXP);
            ObjectNode redisData  = ObjectUtil.buildObjectNodeFromMap(eventData, objectMapper);
            redisData.put(AuthConstant.TYPE, AuthConstant.ACCESS_TOKEN);
            redisService.save(key, redisData, Duration.ofMillis(exp));
        } catch (Exception ex){
            LOGGER.error("While processing event for redis access token update ", ex);
        }
    }
}
