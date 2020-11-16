package com.dancers.service.system.domain.event;

import com.dancers.service.system.domain.SystemOrg;
import org.springframework.context.ApplicationEvent;

public class SystemOrgSavedEvent extends ApplicationEvent {
    public SystemOrgSavedEvent(SystemOrg menu) {
        super(menu);
    }
}
