package org.tommap.tomuserloginrestapis.event.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.event.application_event.ApplicationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringEventPublisher implements EventPublisher<ApplicationEvent> {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(ApplicationEvent event) {
        log.info("publishing event {}", event.getClass().getSimpleName());

        eventPublisher.publishEvent(event);
    }
}
