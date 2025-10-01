package org.onlyonce.user.core.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String username, String refreshToken, long duration) {
        redisTemplate.opsForValue().set("onlyonce-user-RT:" + username, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("onlyonce-user-RT:" + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("onlyonce-user-RT:" + username);
    }
}
