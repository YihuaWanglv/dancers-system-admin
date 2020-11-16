package com.dancers.service.system.domain.event;

import org.springframework.context.ApplicationEvent;

public class SystemAppSavedEvent extends ApplicationEvent {
    public SystemAppSavedEvent(Long appId) {
        super(appId);
    }
}
