package com.dancers.service.system.domain;

import com.dancers.service.system.domain.event.SystemAppSavedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Entity
@Table(name = "system_app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SystemApp {
    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @GeneratedValue(generator = "JDBC")
    Long appId;
    String appName;
    String appKey;
    //    Long corpId;
    Integer state;
    Integer isDeleted = 0;

    @DomainEvents
    Collection<Object> domainEvents() {
        log.info("calling domainEvents in SystemApp");
        List<Object> events = new ArrayList<>();
        events.add(new SystemAppSavedEvent(this.appId));
        return events;
    }

    @AfterDomainEventPublication
    void callback() {
        log.info("calling callback in SystemApp");
    }

    public void fixDefaultField() {
        if (state == null) state = 1;
    }
}
