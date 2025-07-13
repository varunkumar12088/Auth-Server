package com.academy.auth.processor;

import com.academy.auth.client.EmailClient;
import com.academy.auth.constant.EventType;
import com.academy.auth.entity.User;
import com.academy.common.domain.Event;
import com.academy.common.processor.Processor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationProcessor implements Processor<Event> {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationProcessor.class);

    @Autowired
    private EmailClient emailClient;

    @Override
    public boolean canProcess(Event event) {
        if (ObjectUtils.isNotEmpty(event) || ObjectUtils.isNotEmpty(event.getData())) {
            logger.warn("Received null event or data, cannot process.");
            return false;
        }
        if (StringUtils.equalsAnyIgnoreCase(event.getType(), EventType.EMAIL_VERIFICATION.name())) {
            logger.info("EmailVerificationProcessor can process event: {}", event);
            return true;
        }
        return false;
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing event: {}", event);
        User user = (User) event.getData();
        emailClient.sendEmailVerification(user);
    }


}
