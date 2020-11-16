package com.dancers.service.system.domain.event;

import org.springframework.context.ApplicationEvent;

public class SystemCorpSavedEvent extends ApplicationEvent {
    public SystemCorpSavedEvent(Long corpId) {
        super(corpId);
    }
}
