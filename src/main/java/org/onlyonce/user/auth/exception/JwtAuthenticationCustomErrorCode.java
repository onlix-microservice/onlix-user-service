package org.onlyonce.user.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.onlyonce.user.core.exception.BaseErrorCode;

/**
 * ErrorCode Code 규칙: "AU" + HttpStatusCode
 * - ex) AU400, AU404, AU500
 */
@Getter
@AllArgsConstructor
public enum JwtAuthenticationCustomErrorCode implements BaseErrorCode {

    INVALID_ACCESS_TOKEN("AU401", 401, "유효하지 않은 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("AU401", 401, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN("AU401", 401, "유효하지 않은 리프레시토큰입니다."),
    ACCESS_DENIED("AU403", 403, "인가되지 않은 토큰입니다.");

    private final String code;
    private final int statusCode;
    private final String message;
}
