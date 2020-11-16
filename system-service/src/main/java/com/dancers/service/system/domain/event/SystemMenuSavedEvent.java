package com.dancers.service.system.domain.event;

import com.dancers.service.system.domain.SystemMenu;
import org.springframework.context.ApplicationEvent;

public class SystemMenuSavedEvent extends ApplicationEvent {
    public SystemMenuSavedEvent(SystemMenu menu) {
        super(menu);
    }
}
