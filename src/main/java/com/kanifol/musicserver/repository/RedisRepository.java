package com.kanifol.musicserver.repository;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.RedisClient;

import java.util.List;
import java.util.Set;

@Repository
public class RedisRepository {

    private static final Long TTL = 3600L;
    private static final String GENRES_KEY_POSTFIX = "_genres";
    private final RedisClient redisClient;

    public RedisRepository(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public void addUsedIdsForUser(String username, List<String> ids) {
        redisClient.sadd(username, ids.toArray(new String[0]));
        redisClient.expire(username, TTL);
    }

    public Set<String> getUsedIdsForUser(String username) {
        return redisClient.smembers(username);
    }

    public void addUserFavoriteGenres(String username, Set<String> genres) {
        String redisKey = username + GENRES_KEY_POSTFIX;
        redisClient.sadd(redisKey, genres.toArray(new String[0]));
    }

    public Set<String> getUserFavoriteGenres(String username) {
        String redisKey = username + GENRES_KEY_POSTFIX;
        return redisClient.smembers(redisKey);
    }
}
