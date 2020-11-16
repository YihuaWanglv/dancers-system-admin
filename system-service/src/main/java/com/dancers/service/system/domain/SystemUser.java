package com.dancers.service.system.domain;

import com.dancers.service.system.domain.event.SystemUserSavedEvent;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
@ToString
@Slf4j
@Entity
@Table(name = "system_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SystemUser {
    @Id
    @GeneratedValue(generator = "JDBC")
    Long userId;
    @NotNull
    String username;
    @NotNull
    String mobile;

    @JsonIgnore
    String password;
    @JsonIgnore
    String salt;
    String realName;
    Integer sex;
    Integer state;
    @JsonIgnore
    Integer isDeleted = 0;

    @Column(updatable = false)
    @JsonIgnore
    Long createUser;
    @JsonIgnore
    Long updateUser;
    @Column(updatable = false)
    Date createTime;
    @JsonIgnore
    Date updateTime;

    @JsonIgnore
    @JoinTable(name = "system_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ManyToMany(
            targetEntity = SystemRole.class
    )
    Set<SystemRole> systemRoles;

    @Transient
    List<Long> roleIdList;

    @Transient
    String roleIds;

    @Transient
    String roleNames;

    @Transient
    String oldPasswordInput;
    @Transient
    String passwordInput;

    @DomainEvents
    Collection<Object> domainEvents() {
        log.info("calling domainEvents in SystemUser");
        List<Object> events = new ArrayList<>();
        events.add(new SystemUserSavedEvent(this));
        return events;
    }

    @AfterDomainEventPublication
    void callback() {
        log.info("calling callback in SystemUser");
    }

    public void fixDefaultField() {
        if (state == null) state = 1;
    }

    public boolean itIsDeleted() {
        return 1 == isDeleted;
    }

    public void create(Long createUser) {
        createTime = new Date();
        if (createUser != null)
            this.createUser = createUser;
    }

    public void update(Long updateUser) {
        updateTime = new Date();
        if (updateUser != null)
            this.updateUser = updateUser;
    }

    public void roleIdListToRoles() {
        if (roleIdList != null) {
            roleIdList.forEach(roleId -> {
                if (systemRoles == null) {
                    systemRoles = new HashSet<>();
                }
                systemRoles.add(SystemRole.builder().roleId(roleId).build());
            });
        }
    }

    public void rolesToRoleIdList() {
//        this.roleIdList = this.systemRoles.stream().map(SystemRole::getRoleId).collect(Collectors.toList());
//        log.info(this.getSystemRoles().toString());

    }

    public void roleIdsToRoleIdList() {
        if (roleIdList == null) roleIdList = new ArrayList<>();
        if (StringUtils.isNotEmpty(roleIds)) {
            roleIdList = Arrays.stream(roleIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
        }
    }
}
