package org.tommap.tomuserloginrestapis.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.event.application_event.UserRegisterEvent;
import org.tommap.tomuserloginrestapis.service.IEmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisterEventHandler implements EventHandler<UserRegisterEvent> {
    private final IEmailService emailService;

    @Override
    @EventListener
    public void handle(UserRegisterEvent event) {
        log.info("start processing user-register-event");

        emailService.sendEmailVerification(
                event.getEmail(),
                event.getVerificationToken(),
                event.getEmailVerificationExpiry(),
                event.getFullName()
        );
    }
}
