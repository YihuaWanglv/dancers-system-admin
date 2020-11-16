package com.dancers.service.system.handler;

import com.dancers.service.system.domain.event.SystemCorpSavedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class DomainEventHandler {

    @Async
    @TransactionalEventListener
    public void HandleSystemCorpSavedEvent(SystemCorpSavedEvent event) {
        log.info("handling SystemCorpSavedEvent, data=" + event.getSource());
        Long corpId = (Long) event.getSource();
    }

}
