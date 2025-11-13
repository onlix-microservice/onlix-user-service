package org.onlyonce.user.core.dto;


import org.onlyonce.user.core.exception.BaseErrorCode;

/**
 * [Common] API 호출 결과의 공통 응답 DTO
 */
public record ApiResponseDto<T> (
        String code,
        String message,
        T data
) {
    private static final String SUCCESS_CODE = "200"; // 성공 상태코드
    private static final String SUCCESS_MESSAGE = "성공"; // 성공 메세지

    /**
     * [Error] API 호출 에러 시, 오류 응답을 위한 정적 메서드
     *
     * @param errorCode
     * @return {@link ApiResponseDto}
     */
    public static ApiResponseDto<Void> error(BaseErrorCode errorCode) {
        return new ApiResponseDto<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * [Error] API 호출 에러 시, 오류 응답을 위한 정적 메서드
     * Exception의 상세한 Message 전달을 위함(ex) 유효성, 결제 방식)
     *
     * @param code
     * @param message
     * @return {@link ApiResponseDto}
     */
    public static ApiResponseDto<Void> error(String code, String message) {
        return new ApiResponseDto<>(code, message, null);
    }

    /**
     * [Success] API 호출 성공 시, 성공 응답을 위한 정적 메서드
     * @param data
     * @return {@link ApiResponseDto}
     */
    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }
}