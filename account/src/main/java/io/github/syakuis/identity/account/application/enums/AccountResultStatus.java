package io.github.syakuis.identity.account.application.enums;

import io.github.syakuis.identity.core.web.ResultStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-10
 */
@RequiredArgsConstructor
public enum AccountResultStatus implements ResultStatus {
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "비밀번호가 유효하지 않습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."),
    EXISTS_USERNAME(HttpStatus.CONFLICT, "존재하는 계정입니다."),
    REGISTERED_ACCOUNT(HttpStatus.CONFLICT, "이미 가입한 계정입니다.")
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
