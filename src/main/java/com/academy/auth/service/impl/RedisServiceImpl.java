package com.academy.auth.service.impl;

import com.academy.auth.exception.DataNotFoundException;
import com.academy.auth.service.RedisService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOps;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    @Override
    public void save(String key, Object value, Duration duration) {
        LOGGER.debug("save object with key : {}", key);
        this.valueOps.set(key, value, duration);
    }

    @Override
    public Object get(String key) {
        Object value = this.valueOps.get(key);
        if(ObjectUtils.isEmpty(value)){
            throw new DataNotFoundException("Value is not present in redis cache for key " + key);
        }
        return value;
    }

    @Override
    public void delete(String key) {
        this.redisTemplate.delete(key);
    }


    @Override
    public Set<String> getKeys(String keySubstring) {
        return this.redisTemplate.keys("*" + keySubstring + "*");
    }

    @Override
    public void deleteKeysContaining(String keySubstring) {
        Set<String> keys = this.getKeys(keySubstring);
        if(!CollectionUtils.isEmpty(keys)){
            this.redisTemplate.delete(keys);
        }
    }

    @Override
    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }
}

