package org.onlyonce.user.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onlyonce.user.auth.exception.JwtAuthenticationCustomErrorCode;
import org.onlyonce.user.core.exception.BaseErrorCode;
import org.onlyonce.user.exception.ErrorResponseDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        log.warn("권한 실패 요청: {} {} -> {}",
                request.getMethod(),
                request.getRequestURI(),
                accessDeniedException.getMessage());

        BaseErrorCode errorCode = JwtAuthenticationCustomErrorCode.ACCESS_DENIED;

        ErrorResponseDto errorResponse = new ErrorResponseDto(
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
