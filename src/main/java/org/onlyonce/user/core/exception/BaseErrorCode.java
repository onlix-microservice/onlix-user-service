package org.onlyonce.user.core.exception;

/**
 * [Common] 공통 에러 코드 인터페이스
 * 모든 전역(Global) 및 도메인 별 ErrorCode Enum은 이 인터페이스를 구현
 */
public interface BaseErrorCode {
    String getCode();
    int getStatusCode();
    String getMessage();
    String name();
}