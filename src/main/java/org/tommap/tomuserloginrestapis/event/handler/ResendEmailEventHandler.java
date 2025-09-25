package org.tommap.tomuserloginrestapis.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.event.application_event.ResendEmailEvent;
import org.tommap.tomuserloginrestapis.service.IEmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResendEmailEventHandler {
    private final IEmailService emailService;

    @EventListener
    @Async("emailTaskExecutor")
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
