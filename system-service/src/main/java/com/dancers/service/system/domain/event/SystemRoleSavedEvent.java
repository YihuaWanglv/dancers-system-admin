package com.dancers.service.system.domain.event;

import com.dancers.service.system.domain.SystemRole;
import org.springframework.context.ApplicationEvent;

public class SystemRoleSavedEvent extends ApplicationEvent {
    public SystemRoleSavedEvent(SystemRole role) {
        super(role);
    }
}
