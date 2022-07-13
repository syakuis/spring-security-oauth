package io.github.syakuis.identity.account.application;

import io.github.syakuis.identity.account.application.exception.AccountResultStatusException;
import io.github.syakuis.identity.core.web.ErrorResponsePayload;
import io.github.syakuis.identity.core.web.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-10
 */
@Slf4j
@ControllerAdvice(annotations = RestController.class)
final class AccountResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountResultStatusException.class)
    ResponseEntity<Object> handleAccountResultStatus(AccountResultStatusException e, WebRequest request) {
        ResultStatus accountResultStatus = e.getAccountResultStatus();

        ErrorResponsePayload payload = ErrorResponsePayload.builder()
            .message(StringUtils.hasText(accountResultStatus.message()) ? accountResultStatus.message() : e.getLocalizedMessage())
            .status(accountResultStatus.name())
            .code(accountResultStatus.httpStatus().value())
            .build();

        log.trace(e.getLocalizedMessage(), e);

        return handleExceptionInternal(e, payload, new HttpHeaders(), accountResultStatus.httpStatus(), request);
    }
}
