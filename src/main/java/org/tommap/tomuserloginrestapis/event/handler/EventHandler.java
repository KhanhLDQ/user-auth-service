package org.tommap.tomuserloginrestapis.event.handler;

import org.tommap.tomuserloginrestapis.event.UserGenericEvent;

public interface EventHandler <T extends UserGenericEvent> {
    void handle(T event);
}
