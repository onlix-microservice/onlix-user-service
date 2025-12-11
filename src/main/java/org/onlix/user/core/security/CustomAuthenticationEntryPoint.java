package org.onlix.user.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onlix.user.auth.exception.JwtAuthenticationCustomErrorCode;
import org.onlix.user.auth.exception.JwtAuthenticationCustomException;
import org.onlix.user.core.exception.BaseErrorCode;
import org.onlix.user.core.dto.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("인증 실패 요청: {} {} -> {}",
                request.getMethod(),
                request.getRequestURI(),
                authException.getMessage());

        String authHeader = request.getHeader("Authorization");
        log.debug("Authorization Header: {}", authHeader);

        BaseErrorCode errorCode;

        if (authException instanceof JwtAuthenticationCustomException jwtEx) {
            errorCode = jwtEx.getErrorCode();
        } else {
            errorCode = JwtAuthenticationCustomErrorCode.INVALID_ACCESS_TOKEN;
        }

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        response.setStatus(errorCode.getStatusCode());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
