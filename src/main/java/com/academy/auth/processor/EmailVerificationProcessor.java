package com.academy.auth.processor;

import com.academy.auth.constant.EventType;
import com.academy.auth.entity.User;
import com.academy.common.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationProcessor extends AbstractProcessor<Event> {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationProcessor.class);

    @Override
    public boolean canProcess(Event event) {
        return this.canProcess(event, EventType.EMAIL_VERIFICATION);
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing event: {}", event);
        User user = (User) event.getData();
        emailClient.sendEmailVerification(user);
    }


}
