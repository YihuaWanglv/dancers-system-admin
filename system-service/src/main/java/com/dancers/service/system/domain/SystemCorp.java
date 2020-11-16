package com.dancers.service.system.domain;

import com.dancers.service.system.domain.event.SystemCorpSavedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Entity
@Table(name = "system_corp")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SystemCorp {
    @Id
    @GeneratedValue(generator = "JDBC")
    Long corpId;
    String corpKey;
    String corpName;
    Integer state;
    Integer isDeleted = 0;

    @DomainEvents
    Collection<Object> domainEvents() {
        log.info("calling domainEvents in BaseTenant");
        List<Object> events = new ArrayList<>();
        events.add(new SystemCorpSavedEvent(this.corpId));
        return events;
    }

    @AfterDomainEventPublication
    void callback() {
        log.info("calling callback in BaseTenant");
    }

    public void fixDefaultField() {
        if (state == null) state = 1;
    }
}
