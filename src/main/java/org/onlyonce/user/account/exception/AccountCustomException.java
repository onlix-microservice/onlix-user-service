package org.onlyonce.user.account.exception;

import lombok.Getter;
import org.onlyonce.user.core.exception.BaseErrorCode;

@Getter
public class AccountCustomException extends RuntimeException {

    private final BaseErrorCode errorCode;

    // 기본 메시지 사용
    public AccountCustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 보다 상세한 Message를 로그로 남기거나, 응답 메세지로 제공할 경우
    public AccountCustomException(BaseErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}
