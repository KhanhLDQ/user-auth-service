package org.tommap.tomuserloginrestapis.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.event.application_event.PasswordResetEvent;
import org.tommap.tomuserloginrestapis.service.IEmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordResetHandler {
    private final IEmailService emailService;

    @EventListener
    @Async("emailTaskExecutor")
    public void handle(PasswordResetEvent event) {
        log.info("start processing password-reset-event");

        emailService.sendEmailVerification( //email should include UI page instead of email verification URL
                event.getEmail(),
                event.getVerificationToken(),
                event.getEmailVerificationExpiry(),
                event.getFullName()
        );
    }
}
