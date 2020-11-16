package com.dancers.service.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.dancers.service.system.core.Keys;
import com.dancers.service.system.domain.SystemRole;
import com.dancers.service.system.domain.SystemUser;
import com.dancers.service.system.domain.SystemUserRepository;
import com.dancers.service.system.exceptions.AuthorizedFailedException;
import com.dancers.service.system.exceptions.NotFoundException;
import com.dancers.service.system.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemUserService {
    @Resource
    SystemUserRepository systemUserRepository;

    public void create(SystemUser user) {
        Assert.isTrue(StringUtils.isNotEmpty(user.getPasswordInput()), "创建用户时，请录入用户的登录密码");
        Assert.isTrue(StringUtils.isNotEmpty(user.getUsername()), "请录入用户的用户名称");
        String passEncrypt = PasswordHelper.superEncrypt(user.getPasswordInput(), Keys.User.Password.SALT);
        user.setPassword(passEncrypt);
        user.setSalt(Keys.User.Password.SALT);
        user.fixDefaultField();
        user.create(null);
        user.roleIdListToRoles();
        systemUserRepository.save(user);
    }

    public void delete(Long id) {
        systemUserRepository.save(SystemUser.builder().userId(id).isDeleted(1).build());
    }


    public void update(SystemUser obj) {
        log.info("update user: {}", obj.toString());
        SystemUser user = systemUserRepository.findById(obj.getUserId()).orElseThrow(EntityNotFoundException::new);
        if (user.itIsDeleted())
            throw new NotFoundException();
        if (StringUtils.isNotEmpty(obj.getOldPasswordInput())) {
            if (StringUtils.isNotEmpty(user.getPassword())) {
                boolean flag = PasswordHelper.isPasswordRight(user.getPassword(), user.getSalt(), obj.getOldPasswordInput());
                if (!flag) {
                    throw new AuthorizedFailedException(null);
                }
            }
            String passEncrypt = PasswordHelper.superEncrypt(obj.getPasswordInput(), Keys.User.Password.SALT);
            obj.setPassword(passEncrypt);
            obj.setSalt(Keys.User.Password.SALT);
        } else if (StringUtils.isNotEmpty(obj.getPasswordInput())) {
            String passEncrypt = PasswordHelper.superEncrypt(obj.getPasswordInput(), Keys.User.Password.SALT);
            obj.setPassword(passEncrypt);
            obj.setSalt(Keys.User.Password.SALT);
        } else {
            obj.setPassword(null);
        }
        obj.update(null);
        obj.roleIdListToRoles();
        BeanUtil.copyProperties(obj, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        systemUserRepository.save(user);
    }

    public SystemUser getById(Long id) {
        SystemUser systemUser = systemUserRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        systemUser.setRoleIdList(systemUserRepository.getRoleIdListByUserId(systemUser.getUserId()));
        Set<SystemRole> systemRoles = systemUser.getSystemRoles();
        if (systemRoles != null) {
            systemUser.setRoleNames(systemRoles.stream().map(SystemRole::getRoleName).collect(Collectors.joining(",")));
        }
        return systemUser;
    }

    public List<SystemUser> list(SystemUser user) {
        return systemUserRepository.findAll(Example.of(user));
    }

    public Page<SystemUser> list(SystemUser user, Pageable pageable) {
        Page<SystemUser> list = systemUserRepository.findAll(Example.of(user), pageable);
        if (list.hasContent()) {
            List<Long> userIdList = list.get().map(SystemUser::getUserId).collect(Collectors.toList());
            List<SystemUserRepository.UserRoleIds> userRoleIds = systemUserRepository.getRoleIdsByUserIdList(userIdList);
            if (userRoleIds != null) {
                Map<Long, String> userRoleIdsMap = userRoleIds.stream().collect(Collectors.toMap(
                        SystemUserRepository.UserRoleIds::getUserId, SystemUserRepository.UserRoleIds::getRoleIds));
                log.info("userRoleIdsMap={}", userRoleIdsMap.toString());
                list.get().forEach(systemUser -> {
                    if (userRoleIdsMap.containsKey(systemUser.getUserId())) {
                        systemUser.setRoleIds(userRoleIdsMap.get(systemUser.getUserId()));
                        systemUser.roleIdsToRoleIdList();
                    }
                });
            }
        }
        return list;
    }
}
