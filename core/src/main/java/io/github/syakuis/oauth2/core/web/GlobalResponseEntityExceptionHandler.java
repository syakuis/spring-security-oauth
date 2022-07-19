package io.github.syakuis.oauth2.core.web;

import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-31
 */
@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleConstraintViolation(IllegalArgumentException e, WebRequest request) {
        return handleExceptionInternal(e, new ErrorResponsePayload(HttpStatus.BAD_REQUEST, e.getLocalizedMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    // todo 유효성 검증 공통 구현
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        ValidationErrorResponsePayload payload = ValidationErrorResponsePayload.validBuilder()
            .httpStatus(DefaultResultStatus.INVALID_ARGUMENT.httpStatus())
            .message(DefaultResultStatus.INVALID_ARGUMENT.message())
            .details(e.getConstraintViolations().stream().map(v -> ValidationErrorResponsePayload.Details.builder()
                    .target(v.getPropertyPath().toString())
                    .message(v.getMessage())
                    .code(v.getMessageTemplate())
                    .build()).toList())
            .build();

        return handleExceptionInternal(e, payload.wrapper(), new HttpHeaders(), DefaultResultStatus.INVALID_ARGUMENT.httpStatus(), request);
    }
}
