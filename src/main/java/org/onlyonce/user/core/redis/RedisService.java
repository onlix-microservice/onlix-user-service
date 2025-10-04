package org.onlyonce.user.core.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * RefreshToken 저장
     * RT-1) {loginId}:{deviceId} -> refreshToken
     * RT-2) {refreshToken} -> {loginId}:{deviceId}
     */
    public void saveRefreshToken(String loginId, String deviceId, String refreshToken, long duration) {

        // {userId}:{deviceId} -> refreshToken
        redisTemplate.opsForValue()
                .set("onlix-user-RT:" + loginId + ":" + deviceId, refreshToken, duration, TimeUnit.MILLISECONDS);
        // refreshToken -> {userId}:{deviceId}
        redisTemplate.opsForValue()
                .set("onlix-user-RTIDX:" + refreshToken, loginId + ":" + deviceId, duration, TimeUnit.MILLISECONDS);
    }

    /**
     * RefreshToken으로 유저:디바이스 조회
     * ex) {refreshToken} -> {loginId}:{deviceId}
     */
    public String getUserKeyByRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get("onlix-user-RT:" + refreshToken);
    }

    public void deleteByRefreshToken(String refreshToken) {
        String userKey = getUserKeyByRefreshToken(refreshToken);

        if (userKey != null) {
            redisTemplate.delete("onlix-user-RT:" + userKey);
            redisTemplate.delete("onlix-user-RTIDX:" + refreshToken);
        }
    }

    /**
     * 특정 유저의 모든 기기 로그아웃
     */
    public void deleteAllByUser(String loginId) {
        Set<String> keys = redisTemplate.keys("onlix-user-RT:" + loginId + ":*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
