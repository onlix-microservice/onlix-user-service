package org.onlyonce.user.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.onlyonce.user.account.exception.AccountCustomException;
import org.onlyonce.user.auth.exception.JwtAuthenticationCustomException;
import org.onlyonce.user.core.dto.ApiResponseDto;
import org.onlyonce.user.core.utils.LogUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 전역 예외 처리 핸들러
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * [Exception] JSON 파싱에 실패한 경우
     * - Http Status: 400(Bad Request)
     * - Log Level : Warn
     * - Log Output : [400] INVALID_JSON - 유효하지 않은 JSON 형식입니다.
     *
     * @param ex HttpMessageNotReadableException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotReadableException(HttpMessageNotReadableException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_JSON;

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode));
    }

    /**
     * [Exception] 필수 요청 파라미터가 누락된 경우
     * - Http Status: 400(Bad Request)
     * - Log Level : Warn
     * - Log Output : [400] MISSING_REQUIRED_PARAMETER - 필수 요청 파라미터가 누락되었습니다.
     *
     * @param ex MissingServletRequestParameterException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleMissingParameterException(MissingServletRequestParameterException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.MISSING_REQUIRED_PARAMETER;

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode));
    }

    /**
     * [Exception] 요청 파라미터 타입이 잘못된 경우
     * - Http Status: 400(Bad Request)
     * - Log Level : Warn
     * - Log Output : [400] INVALID_PARAMETER_TYPE - 요청 파라미터 타입이 잘못되었습니다.
     *
     * @param ex MethodArgumentTypeMismatchException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotValidException(MethodArgumentTypeMismatchException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_PARAMETER_TYPE;

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode));
    }

    /**
     * [Exception] 유효성 검증에 실패한 경우
     * - Http Status: 400(Bad Request)
     * - Log Level : Warn
     * - Log Output : [400] VALIDATION_FAILED - 유효성 검증에 실패하였습니다.
     *
     * @param ex MethodArgumentNotValidException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotValidException(MethodArgumentNotValidException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.VALIDATION_FAILED;

        LogUtils.warn(errorCode);

        // 첫 번째 필드 오류 메시지 추출
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("유효하지 않은 요청입니다.");

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode.getCode(), errorMessage));
    }

    /**
     * [Exception] 요청한 자원이 없는 경우
     * - Http Status: 404(Not Found)
     * - Log Level : Warn
     * - Log Output : [400] NOT_FOUND - 요청하신 자원을 찾을 수 없습니다.
     *
     * @param ex NoHandlerFoundException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotFoundException(NoHandlerFoundException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.NOT_FOUND;

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode));
    }

    /**
     * [Exception]
     * - Http Status: 405(Method Not Allowed)
     * - Log Level : Warn
     * - Log Output : [405] METHOD_NOT_ALLOWED - 잘못된 HTTP 메서드 요청입니다.
     *
     * @param ex MethodNotAllowedException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNotAllowedMethodException(MethodNotAllowedException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.METHOD_NOT_ALLOWED;

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode));
    }

    /**
     * [Exception] 서버 내부 예외 처리 (예상하지 못한 모든 예외)
     * - Http Status: 500(Internal Server Error)
     * - Log Level : Error
     * - Log Output : [500] INTERNAL_SERVER_ERROR - 서버 내부 오류가 발생했습니다.
     *                      + Exception Stack Trace
     *
     * @param ex Exception
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleInternalServerError(Exception ex) {
        BaseErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;

        LogUtils.error(errorCode, ex);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode));
    }

    /**
     * [Account Custom Exception]
     * - Http Status: AccountCustomException의 status code
     * - Log Level : Warn
     *
     * @param ex AccountCustomException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(AccountCustomException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleAccountCustomException(AccountCustomException ex) {
        BaseErrorCode errorCode = ex.getErrorCode();

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode.getCode(), ex.getMessage()));
    }

    /**
     * [JwtAuthentication Custom Exception]
     * - Http Status: JwtAuthenticationCustomException의 status code
     * - Log Level : Warn
     *
     * @param ex JwtAuthenticationCustomException
     * @return {@link ResponseEntity<ApiResponseDto>}
     */
    @ExceptionHandler(JwtAuthenticationCustomException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleJwtAuthenticationCustomException(JwtAuthenticationCustomException ex) {
        BaseErrorCode errorCode = ex.getErrorCode();

        LogUtils.warn(errorCode);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponseDto.error(errorCode.getCode(), ex.getMessage()));
    }
}