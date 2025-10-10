package org.onlyonce.user.core.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.onlyonce.user.auth.exception.JwtAuthenticationCustomErrorCode;
import org.onlyonce.user.auth.exception.JwtAuthenticationCustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // 헤더에서 토큰 확인 (모바일 앱 등 호환용)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 헤더에 토큰 없으면 Secure HttpOnly Cookie에서 확인
        if (token == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token); // 토큰에 있는 정보 꺼내기
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
            } else {
                // 인증 실패 시 AuthenticationEntryPoint로 위임
                AuthenticationException authException = new JwtAuthenticationCustomException(JwtAuthenticationCustomErrorCode.INVALID_ACCESS_TOKEN);
                authenticationEntryPoint.commence(request, response, authException);
            }
        } catch (ExpiredJwtException e) {
            // 만료된 토큰 예외 처리
            AuthenticationException authException = new JwtAuthenticationCustomException(JwtAuthenticationCustomErrorCode.EXPIRED_ACCESS_TOKEN);
            authenticationEntryPoint.commence(request, response, authException);
        } catch (JwtException e) {
            // 기타 JWT 예외 처리
            AuthenticationException authException = new JwtAuthenticationCustomException(JwtAuthenticationCustomErrorCode.INVALID_ACCESS_TOKEN);
            authenticationEntryPoint.commence(request, response, authException);
        }
    }
}