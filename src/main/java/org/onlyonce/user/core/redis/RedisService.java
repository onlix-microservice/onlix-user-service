package org.onlyonce.user.core.redis;

import org.apache.commons.codec.digest.DigestUtils;
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
     * RT-3) {loginId} -> {deviceId Set}
     */
    public void saveRefreshToken(String loginId, String deviceId, String refreshToken, long duration) {
        String hashedUserKey = DigestUtils.sha256Hex(loginId + ":" + deviceId);

        // {userId}:{deviceId} -> refreshToken
        redisTemplate.opsForValue()
                .set("onlix-user-RT:" + hashedUserKey, refreshToken, duration, TimeUnit.MILLISECONDS);
        // refreshToken -> {userId}:{deviceId}
        redisTemplate.opsForValue()
                .set("onlix-user-RTIDX:" + refreshToken, loginId + ":" + deviceId, duration, TimeUnit.MILLISECONDS);

        // RTLIST: loginId -> {deviceId, ...}
        redisTemplate.opsForSet().add("onlix-user-RTLIST:" + loginId, deviceId);
    }

    /**
     * RefreshToken으로 유저:디바이스 조회
     * ex) {refreshToken} -> {loginId}:{deviceId}
     */
    public String getUserKeyByRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get("onlix-user-RTIDX:" + refreshToken);
    }

    // 로그아웃 시, RefreshToken 삭제
    public void deleteByRefreshToken(String refreshToken) {
        String userKey = getUserKeyByRefreshToken(refreshToken);

        if (userKey == null) {
            return;
        }
        String hashedUserKey = DigestUtils.sha256Hex(userKey);

        redisTemplate.delete("onlix-user-RT:" + hashedUserKey);
        redisTemplate.delete("onlix-user-RTIDX:" + refreshToken);

        // RTLIST에서 해당 deviceId 제거
        String[] parts = userKey.split(":");
        if (parts.length == 2) {
            String loginId = parts[0];
            String deviceId = parts[1];
            redisTemplate.opsForSet().remove("onlix-user-RTLIST:" + loginId, deviceId);
        }
    }

    /**
     * 특정 유저의 모든 기기 로그아웃
     */
    public void deleteAllByUser(String loginId) {
        Set<String> deviceIds = redisTemplate.opsForSet().members("onlix-user-RTLIST:" + loginId);
        if (deviceIds == null || deviceIds.isEmpty()) return;

        for (String deviceId : deviceIds) {
            String hashedUserKey = DigestUtils.sha256Hex(loginId + ":" + deviceId);
            String rtKey = "onlix-user-RT:" + hashedUserKey;

            String refreshToken = redisTemplate.opsForValue().get(rtKey);
            redisTemplate.delete(rtKey);
            redisTemplate.delete("onlix-user-RTIDX:" + refreshToken);
        }

        redisTemplate.delete("onlix-user-RTLIST:" + loginId);
    }
}
