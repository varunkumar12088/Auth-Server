package com.academy.auth.processor;

import com.academy.auth.constant.AppName;
import com.academy.auth.constant.AuthConstant;
import com.academy.auth.constant.EventType;
import com.academy.auth.service.RedisService;
import com.academy.common.domain.Event;
import com.academy.common.util.KeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class TokenRemoveProcessor extends AbstractProcessor<Event>{

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRemoveProcessor.class);

    @Autowired
    private RedisService redisService;

    @Override
    public boolean canProcess(Event event) {
        return this.canProcess(event, EventType.TOKEN_REMOVE);
    }

    @Override
    public void process(Event event) {
        LOGGER.debug("Processing event for token remove");
        try {
            Map<String, Object> eventData = (Map<String, Object>) event.getData();
            String appName = (String) eventData.getOrDefault(AuthConstant.APP_NAME, AppName.DEFAULT.name());
            String username = (String) eventData.getOrDefault(AuthConstant.USERNAME, "");
            String jti = (String) eventData.getOrDefault(AuthConstant.JTI, UUID.randomUUID().toString());
            String key = KeyUtil.redisTokenKey(appName, username, jti);
            redisService.deleteKeysContaining(key);
        } catch (Exception ex){
            LOGGER.error("While processing event for token remove ", ex);
        }
    }
}
