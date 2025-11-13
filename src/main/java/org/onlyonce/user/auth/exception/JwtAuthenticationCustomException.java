package org.onlyonce.user.auth.exception;

import lombok.Getter;
import org.onlyonce.user.core.exception.BaseErrorCode;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationCustomException extends AuthenticationException {

    private final BaseErrorCode errorCode;

    // 기본 메시지 사용
    public JwtAuthenticationCustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 보다 상세한 Message를 로그로 남기거나, 응답 메세지로 제공할 경우
    public JwtAuthenticationCustomException(BaseErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}
