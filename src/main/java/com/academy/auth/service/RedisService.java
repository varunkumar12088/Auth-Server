package com.academy.auth.service;

import java.time.Duration;
import java.util.Set;

public interface RedisService {

    void save(String key, Object value, Duration duration);

    Object get(String key);

    void delete(String key);


    Set<String> getKeys(String keySubstring);

    void deleteKeysContaining(String keySubstring);

    boolean exists(String key);

}
