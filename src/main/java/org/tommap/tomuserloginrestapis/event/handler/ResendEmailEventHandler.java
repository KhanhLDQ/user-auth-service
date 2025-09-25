package org.tommap.tomuserloginrestapis.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.event.application_event.ResendEmailEvent;
import org.tommap.tomuserloginrestapis.service.IEmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResendEmailEventHandler implements EventHandler<ResendEmailEvent> {
    private final IEmailService emailService;

    @Override
    @EventListener
    public void handle(ResendEmailEvent event) {
        log.info("start processing resend-email-event");

        emailService.sendEmailVerification(
                event.getEmail(),
                event.getVerificationToken(),
                event.getEmailVerificationExpiry(),
                event.getFullName()
        );
    }
}
