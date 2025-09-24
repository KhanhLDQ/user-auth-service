package org.tommap.tomuserloginrestapis.event.application_event;

import lombok.Getter;
import org.tommap.tomuserloginrestapis.event.EventType;
import org.tommap.tomuserloginrestapis.event.UserGenericEvent;

import java.time.LocalDateTime;

@Getter
public abstract class ApplicationEvent implements UserGenericEvent {
    private final EventType eventType;
    private final LocalDateTime timestamp;

    public ApplicationEvent(EventType eventType, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.timestamp = timestamp;
    }
}
