package io.github.syakuis.oauth2.account.application.exception;

import io.github.syakuis.oauth2.core.web.ResultStatus;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public class AccountResultStatusException extends RuntimeException {
    @Getter
    private final ResultStatus accountResultStatus;

    public AccountResultStatusException(ResultStatus accountResultStatus) {
        super(accountResultStatus.message());
        this.accountResultStatus = accountResultStatus;
    }

    public AccountResultStatusException(ResultStatus accountResultStatus, String message) {
        super(message);
        this.accountResultStatus = accountResultStatus;
    }

    public AccountResultStatusException(ResultStatus accountResultStatus, String message, Throwable cause) {
        super(message, cause);
        this.accountResultStatus = accountResultStatus;
    }

    public AccountResultStatusException(ResultStatus accountResultStatus, Throwable cause) {
        super(cause);
        this.accountResultStatus = accountResultStatus;
    }

    public AccountResultStatusException(ResultStatus accountResultStatus, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.accountResultStatus = accountResultStatus;
    }

    public static AccountResultStatusException of(ResultStatus accountResultStatus) {
        return new AccountResultStatusException(accountResultStatus, accountResultStatus.message());
    }
}
