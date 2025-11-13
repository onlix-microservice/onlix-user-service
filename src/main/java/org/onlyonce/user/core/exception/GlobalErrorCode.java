package org.onlyonce.user.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * [Common] HTTP 통신 결과 에러 코드를 enum 형태로 관리(전역 에러 코드)
 * Code Convention
 * - 대표코드 : Http Status + 0
 * - 세부코드 : Http Status + 연속 되는 번호
 *
 * ************ HTTP Error Status Code ************
 * 400(Bad Request)
 * 401(Unauthorized)
 * 403(Forbidden)
 * 404(Not Found)
 * 500(Internal Server Error)
 * etc..
 * ************************************************
 */
@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    BAD_REQUEST("4000", 400, "잘못된 요청입니다."),
    INVALID_JSON("4001", 400, "유효하지 않은 JSON 형식입니다."),
    MISSING_REQUIRED_PARAMETER("4002", 400, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_TYPE("4003", 400, "요청 파라미터 타입이 잘못되었습니다."),
    VALIDATION_FAILED("4004", 400, "유효성 검사에 실패했습니다."),

    NOT_FOUND("4040", 404, "요청하신 자원을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("4050", 405, "잘못된 HTTP 메서드 요청입니다."),

    INTERNAL_SERVER_ERROR("5000", 500, "서버 내부 오류가 발생했습니다."),
    EXTERNAL_SERVICE_ERROR("5030", 503, "외부 API 호출 실패");

    private final String code;
    private final int statusCode;
    private final String message;
}