package com.academy.auth.processor;

import com.academy.auth.constant.EventType;
import com.academy.common.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationFailureProcessor extends AbstractProcessor<Event> {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationFailureProcessor.class);

    @Override
    public boolean canProcess(Event event) {
        return this.canProcess(event, EventType.EMAIL_VERIFICATION_FAILURE);
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing event: {}", event);
        String email = (String) event.getData();
        emailClient.sendEmailVerificationFailure(email);
    }

}
