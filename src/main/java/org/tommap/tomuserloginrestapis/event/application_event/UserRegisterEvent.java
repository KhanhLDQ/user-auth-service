package org.tommap.tomuserloginrestapis.event.application_event;

import lombok.Getter;
import org.tommap.tomuserloginrestapis.event.EventType;

import java.time.LocalDateTime;

@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private final String email;
    private final String verificationToken;
    private final String fullName;

    public UserRegisterEvent(EventType eventType, LocalDateTime timestamp, String email, String verificationToken, String fullName) {
        super(eventType, timestamp);
        this.email = email;
        this.verificationToken = verificationToken;
        this.fullName = fullName;
    }
}
