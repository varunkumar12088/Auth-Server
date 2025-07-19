package com.academy.auth.processor;

import com.academy.auth.constant.EventType;
import com.academy.common.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenUpdateProcessor extends AbstractProcessor<Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTokenUpdateProcessor.class);

    @Override
    public boolean canProcess(Event event) {
        return this.canProcess(event, EventType.REDIS_TOKEN_UPDATE);
    }

    @Override
    public void process(Event event) {

    }
}
