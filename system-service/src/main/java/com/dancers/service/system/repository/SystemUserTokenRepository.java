package com.dancers.service.system.repository;


import com.dancers.service.system.dto.UserDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class SystemUserTokenRepository {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, UserDTO> valueOps;

    public UserDTO get(String key) {
        return valueOps.get(UserDTO.ENTITY_KEY_PREFIX + key);
    }

    /**
     * 设置token缓存， 暂时设置为1小时
     */
    public void set(String key, UserDTO value) {
        valueOps.set(UserDTO.ENTITY_KEY_PREFIX + key, value, 2, TimeUnit.HOURS);
    }

    /**
     * 设置缓存，并设置超时时间，expire单位为秒
     */
    public void set(String key, UserDTO value, Long expire) {
        valueOps.set(UserDTO.ENTITY_KEY_PREFIX + key, value, expire, TimeUnit.SECONDS);
    }

    public void expire(String key) {
        redisTemplate.expire(UserDTO.ENTITY_KEY_PREFIX + key, 0, TimeUnit.SECONDS);
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(UserDTO.ENTITY_KEY_PREFIX + key, timeout, unit);
    }
}
