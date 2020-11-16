package com.dancers.service.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    public static final String ENTITY_KEY_PREFIX = "SYSTEM:USER:TOKEN:";
    Long userId;
    Long roleId;
    List<Long> roleIdList;
    String username;
    String realName;
    String mobile;
    String token;
    String[] access;
    String roleName;
    List<String> adminModuleEnableKeys;
}
