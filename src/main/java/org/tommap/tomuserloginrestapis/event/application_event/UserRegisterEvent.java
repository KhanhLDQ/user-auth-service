package org.tommap.tomuserloginrestapis.event.application_event;

import lombok.Builder;
import lombok.Getter;
import org.tommap.tomuserloginrestapis.event.EventType;

import java.time.LocalDateTime;

@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private final String email;
    private final String verificationToken;
    private final long emailVerificationExpiry;
    private final String fullName;

    @Builder
    public UserRegisterEvent(EventType eventType, LocalDateTime timestamp, String email, String verificationToken, long emailVerificationExpiry, String fullName) {
        super(eventType, timestamp);
        this.email = email;
        this.verificationToken = verificationToken;
        this.emailVerificationExpiry = emailVerificationExpiry;
        this.fullName = fullName;
    }
}
