package org.onlyonce.user.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.onlyonce.user.core.exception.BaseErrorCode;

@Slf4j
public class LogUtils {

    public static void warn(BaseErrorCode errorCode) {
        log.warn("[{}] {} - {}",
                errorCode.getStatusCode(),
                errorCode.name(),
                errorCode.getMessage());
    }

    // Error는 심각한 상황이라, stack trace도 출력
    public static void error(BaseErrorCode errorCode, Exception ex) {
        log.error("[{}] {} - {}",
                errorCode.getStatusCode(),
                errorCode.name(),
                errorCode.getMessage(),
                ex);
    }
}