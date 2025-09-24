package org.tommap.tomuserloginrestapis.event.publisher;

import org.tommap.tomuserloginrestapis.event.UserGenericEvent;

public interface EventPublisher<T extends UserGenericEvent> {
    void publish(T event);
}
