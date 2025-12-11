package org.onlix.user.auth.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.onlix.user.account.domain.entity.Account;
import org.onlix.user.account.domain.repository.AccountRepository;
import org.onlix.user.account.exception.AccountCustomErrorCode;
import org.onlix.user.account.exception.AccountCustomException;
import org.onlix.user.auth.dto.JwtResponse;
import org.onlix.user.auth.dto.LoginRequest;
import org.onlix.user.auth.dto.LoginResponse;
import org.onlix.user.auth.exception.JwtAuthenticationCustomErrorCode;
import org.onlix.user.auth.exception.JwtAuthenticationCustomException;
import org.onlix.user.auth.service.AuthService;
import org.onlix.user.core.redis.RedisService;
import org.onlix.user.core.security.CustomUserDetails;
import org.onlix.user.core.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PasswordService implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final AccountRepository accountRepository;

    // 회원가입
    @Transactional
    public void signin(LoginRequest loginRequestDto) {
        String loginId = loginRequestDto.loginId();
        String password = loginRequestDto.password();

        if (accountRepository.findByLoginId(loginId).isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        Account accountEntity = Account.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .build();
        accountRepository.save(accountEntity);
    }

    // 로그인
    public JwtResponse login(LoginRequest loginRequestDto) {
        String loginId = loginRequestDto.loginId();
        String password = loginRequestDto.password();
        String deviceId = loginRequestDto.deviceId();

        try {
            // 입력한 계정 정보로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password);

            // 인증 시도 -> 내부적으로 CustomAuthenticationProvider 실행
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증 성공 시 UserDetails 조회
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 로그인 성공하면 token 발행
            String accessToken = jwtProvider.generateAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
            String refreshToken = jwtProvider.generateRefreshToken(userDetails.getUsername());

            // RefreshToken Redis 저장(TTL : 7일)
            redisService.saveRefreshToken(loginId, deviceId, refreshToken, jwtProvider.getRefreshTokenExpireTime());

            LoginResponse loginInfo = LoginResponse.fromUserDetails(userDetails);

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtProvider.getAccessTokenExpireTime() / 1000)
                    .refreshExpiresIn(jwtProvider.getRefreshTokenExpireTime() / 1000)
                    .loginInfo(loginInfo)
                    .build();
        } catch (BadCredentialsException dex) {
            throw new AccountCustomException(AccountCustomErrorCode.BAD_CREDENTIALS);
        } catch (DisabledException e) {
            throw new AccountCustomException(AccountCustomErrorCode.ACCOUNT_DISABLED);
        } catch (AuthenticationException e) {
            throw new AccountCustomException(AccountCustomErrorCode.AUTH_FAILED);
        }
    }

    // 토큰 리프레쉬
    public JwtResponse refresh(HttpServletRequest request) {
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        String userKey = redisService.getUserKeyByRefreshToken(refreshToken);

        if (refreshToken == null || userKey == null) {
            throw new JwtAuthenticationCustomException(JwtAuthenticationCustomErrorCode.INVALID_REFRESH_TOKEN);
        }

        String loginId = userKey.split(":")[0];

        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));

        String newAccessToken = jwtProvider.generateAccessToken(loginId, authorities);

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(jwtProvider.getAccessTokenExpireTime() / 1000)
                .build();
    }

    // 로그아웃
    public void logout(HttpServletRequest request) {
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        redisService.deleteByRefreshToken(refreshToken);
    }
}
