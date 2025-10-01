package org.onlyonce.user.account.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.onlyonce.user.core.exception.BaseErrorCode;

/**
 * ErrorCode Code 규칙: "AU" + HttpStatusCode
 * - ex) AU400, AU404, AU500
 */
@Getter
@AllArgsConstructor
public enum AccountCustomErrorCode implements BaseErrorCode {

    AUTH_FAILED("AC400", 400, "인증에 실패하였습니다."),
    BAD_CREDENTIALS("AC401", 401, "아이디 또는 비밀번호가 올바르지 않습니다."),
    ACCOUNT_DISABLED("AC403", 403, "비활성화된 계정입니다."),
    NOT_FOUND("AC404", 404, "해당 계정이 존재하지 않습니다");

    private final String code;
    private final int statusCode;
    private final String message;
}
