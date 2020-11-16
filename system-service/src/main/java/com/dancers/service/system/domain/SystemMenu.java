package com.dancers.service.system.domain;

import com.dancers.service.system.core.Keys;
import com.dancers.service.system.core.tree.TreeNode;
import com.dancers.service.system.core.tree.TreeSelect;
import com.dancers.service.system.domain.event.SystemMenuSavedEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Entity
@Table(name = "system_menu")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SystemMenu implements TreeNode<Long, SystemMenu>, TreeSelect {
    @Id
    @GeneratedValue(generator = "JDBC")
    Long menuId;
    String menuKey;
    Long parentId;
    String menuName;
    String menuType;
//    String menuIcon;
    Integer sort;
    Integer state;
    String menuPath;
    Integer isDeleted = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    List<SystemMenu> children;

    @Transient
    String menuTypeName;

    @DomainEvents
    Collection<Object> domainEvents() {
        log.info("calling domainEvents in SystemMenu");
        List<Object> events = new ArrayList<>();
        events.add(new SystemMenuSavedEvent(this));
        return events;
    }

    @AfterDomainEventPublication
    void callback() {
        log.info("calling callback in SystemMenu");
    }

    public void fixDefaultField() {
        if (state == null) state = 1;
        if (parentId == null) parentId = 0L;
    }

    @JsonIgnore
    @Override
    public Long getPrimaryValue() {
        return this.menuId;
    }

    @JsonIgnore
    @Override
    public Long getParentValue() {
        return this.parentId;
    }

    @Override
    public void addChild(SystemMenu obj) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(obj);
    }

    @JsonIgnore
    @Override
    public boolean isRoot() {
        return parentId != null && parentId == 0;
    }

    @Override
    public String getId() {
        return this.menuId + "";
    }

    @Override
    public String getLabel() {
        return this.menuName;
    }

    public void fixNameFields() {
        if (StringUtils.isNotEmpty(menuType))
            menuTypeName = Keys.MenuType.valueOf(menuType).getAlias();
    }
}