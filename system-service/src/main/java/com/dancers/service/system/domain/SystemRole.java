package com.dancers.service.system.domain;

import com.dancers.service.system.domain.event.SystemRoleSavedEvent;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "roleId")
@Slf4j
@Entity
@Table(name = "system_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SystemRole {
    @Id
    @GeneratedValue(generator = "JDBC")
    Long roleId;
    String roleName;
    String menuIds;
    Integer state;
    @JsonIgnore
    Integer isDeleted = 0;

    @Transient
    List<Long> menuIdList;

    @JsonIgnore
    @Transient
    @ManyToMany(mappedBy = "systemRoles", targetEntity = SystemUser.class)
    Set<SystemUser> systemUsers;

    @DomainEvents
    Collection<Object> domainEvents() {
        log.info("calling domainEvents in SystemRole");
        List<Object> events = new ArrayList<>();
        events.add(new SystemRoleSavedEvent(this));
        return events;
    }

    @AfterDomainEventPublication
    void callback() {
        log.info("calling callback in SystemRole");
    }

    public void menuIdListToMenuIds() {
        if (menuIdList != null) {
            menuIds = menuIdList.stream().map(Object::toString).collect(Collectors.joining(","));
        }
    }

    public void menuIdsToMenuIdList() {
        if (StringUtils.isNotEmpty(menuIds)) {
            menuIdList = Arrays.stream(menuIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
        }
    }

    public void fixDefaultField() {
        if (state == null) state = 1;
    }
}
