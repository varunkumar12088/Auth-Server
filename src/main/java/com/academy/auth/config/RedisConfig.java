package com.academy.auth.config;

import com.academy.auth.constant.AuthConstant;
import com.academy.common.entity.ConfigVar;
import com.academy.common.service.ConfigVarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisConnectionFactory redisConnectionFactory(ConfigVarService<ConfigVar, String> configVarService) {
        String redisHost = configVarService.getConfigVarValue(AuthConstant.REDIS_HOST_ID, String.class);
        Integer redisPort = configVarService.getConfigVarValue(AuthConstant.REDIS_PORT_ID, Integer.class);
        String redisWriteUser = configVarService.getConfigVarValue(AuthConstant.REDIS_USER_WRITE_NAME, String.class);
        String redisWriteUserPass = configVarService.getConfigVarValue(AuthConstant.REDIS_USER_WRITE_PASSWORD, String.class);
        LOGGER.debug("redisHots: {}, redisPort: {}", redisHost, redisPort);

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setUsername(redisWriteUser);
        config.setPassword(RedisPassword.of(redisWriteUserPass));
        LOGGER.debug("RedisStandaloneConfiguration {}", config);

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        LOGGER.debug("Creating redis template");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}

