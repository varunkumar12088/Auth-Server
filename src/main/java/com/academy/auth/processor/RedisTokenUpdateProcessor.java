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
public class RedisTokenUpdateProcessor extends AbstractProcessor<Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTokenUpdateProcessor.class);

    @Autowired
    private RedisService redisService;

    @Override
    public boolean canProcess(Event event) {
        return this.canProcess(event, EventType.REDIS_TOKEN_UPDATE);
    }

    @Override
    public void process(Event event) {
        LOGGER.debug("Processing event for redius token update");
        try {
            Map<String, Object> eventData = (Map<String, Object>) event.getData();
            String appName = (String) eventData.getOrDefault(AuthConstant.APP_NAME, AppName.DEFAULT.name());
            String username = (String) eventData.getOrDefault(AuthConstant.USERNAME, "");
            String jti = (String) eventData.getOrDefault(AuthConstant.JTI, UUID.randomUUID().toString());
            String key = KeyUtil.redisTokenKey(appName, username, jti);
            Long exp = (Long) eventData.getOrDefault(AuthConstant.EXP, AuthConstant.ACCESS_TOKEN_EXP);
            String token = (String) eventData.get(AuthConstant.TOKEN);

        } catch (Exception ex){
            LOGGER.error("While processing event for redius token update");
        }

    }
}
