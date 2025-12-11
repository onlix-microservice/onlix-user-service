package org.onlix.user.core.dto;


import org.onlix.user.core.exception.BaseErrorCode;

/**
 * [Common] API 호출 결과의 공통 응답 DTO
 */
public record ApiResponse<T> (
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
     * @return {@link ApiResponse}
     */
    public static ApiResponse<Void> error(BaseErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * [Error] API 호출 에러 시, 오류 응답을 위한 정적 메서드
     * Exception의 상세한 Message 전달을 위함(ex) 유효성, 결제 방식)
     *
     * @param code
     * @param message
     * @return {@link ApiResponse}
     */
    public static ApiResponse<Void> error(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * [Success] API 호출 성공 시, 성공 응답을 위한 정적 메서드
     * @param data
     * @return {@link ApiResponse}
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }
}