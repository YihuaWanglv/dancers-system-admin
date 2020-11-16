package com.dancers.service.system.domain.event;

import com.dancers.service.system.domain.SystemUser;
import org.springframework.context.ApplicationEvent;

public class SystemUserSavedEvent extends ApplicationEvent {
    public SystemUserSavedEvent(SystemUser user) {
        super(user);
    }
}
