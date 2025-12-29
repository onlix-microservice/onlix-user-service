package org.onlix.user.account.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.onlix.user.core.exception.BaseErrorCode;

/**
 * ErrorCode Code 규칙: "AU" + HttpStatusCode + INDEX
 * - ex) AU4000, AU4040, AU5000
 */
@Getter
@AllArgsConstructor
public enum AccountCustomErrorCode implements BaseErrorCode {

    AUTH_FAILED("AC4000", 400, "인증에 실패하였습니다."),
    BAD_CREDENTIALS("AC4010", 401, "아이디 또는 비밀번호가 올바르지 않습니다."),
    ACCOUNT_DISABLED("AC4030", 403, "비활성화된 계정입니다."),
    NOT_FOUND("AC4040", 404, "해당 계정이 존재하지 않습니다");

    private final String code;
    private final int statusCode;
    private final String message;
}
