package org.onlyonce.user.auth;

import lombok.RequiredArgsConstructor;
import org.onlyonce.user.core.redis.RedisService;
import org.onlyonce.user.core.security.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    // 로그인
    public Map<String, String> login(String username, String password) {
        // DB 없으니 임시로
        if (!"test".equals(username) || !"1234".equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.generateAccessToken(username);
        String refreshToken = jwtProvider.generateRefreshToken(username);

        // RefreshToken → Redis 저장 (7일 TTL)
        redisService.saveRefreshToken(username, refreshToken, 7 * 24 * 60 * 60 * 1000L);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    // 토큰 리프레쉬
    public Map<String, String> refresh(String username, String refreshToken) {
        String savedToken = redisService.getRefreshToken(username);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtProvider.generateAccessToken(username);

        Map<String, String> token = new HashMap<>();
        token.put("accessToken", newAccessToken);

        return token;
    }

    // 로그아웃
    public void logout(String username) {
        redisService.deleteRefreshToken(username);
    }
}
