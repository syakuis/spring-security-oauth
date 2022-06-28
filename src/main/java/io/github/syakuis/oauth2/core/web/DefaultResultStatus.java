package io.github.syakuis.oauth2.core.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-10
 */
@RequiredArgsConstructor
public enum DefaultResultStatus implements ResultStatus {
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "입력이 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),
    ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "오류로 인해 처리할 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String message() {
        return message;
    }
}
