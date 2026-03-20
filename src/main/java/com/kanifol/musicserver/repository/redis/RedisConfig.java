package com.kanifol.musicserver.repository.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.RedisClient;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Bean(destroyMethod = "close")
    public RedisClient redisClient(RedisProperties redisProperties) {
        return RedisClient.builder().hostAndPort(redisProperties.host(), redisProperties.port()).build();
    }
}
