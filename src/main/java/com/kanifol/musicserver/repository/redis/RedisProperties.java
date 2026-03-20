package com.kanifol.musicserver.repository.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redis")
public record RedisProperties(
        String host,
        Integer port
) {}
