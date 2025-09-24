package org.tommap.tomuserloginrestapis.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.event.application_event.UserRegisterEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisterEventHandler implements EventHandler<UserRegisterEvent>{
    @Override
    @EventListener
    public void handle(UserRegisterEvent event) {
        log.info("start processing user-register-event");


    }
}
