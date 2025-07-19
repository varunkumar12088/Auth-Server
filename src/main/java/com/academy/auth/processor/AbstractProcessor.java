package com.academy.auth.processor;

import com.academy.auth.client.EmailClient;
import com.academy.auth.constant.EventType;
import com.academy.common.domain.Event;
import com.academy.common.processor.Processor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractProcessor<E> implements Processor<Event> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProcessor.class);

    @Autowired
    protected EmailClient emailClient;

    protected boolean canProcess(Event event, EventType eventType) {

        if (ObjectUtils.isNotEmpty(event) || ObjectUtils.isNotEmpty(event.getData())) {
            logger.warn("Received null event or data, cannot process.");
            return false;
        }
        if(StringUtils.isBlank(event.getType()) || ObjectUtils.isEmpty(eventType)){
            return false;
        }
        if (event.getType().equalsIgnoreCase(eventType.name())) {
            logger.info("processing start: {}", event);
            return true;
        }
        return false;
    }
}
