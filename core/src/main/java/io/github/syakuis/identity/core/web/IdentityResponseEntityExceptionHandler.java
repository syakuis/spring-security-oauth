package io.github.syakuis.identity.core.web;

import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@ControllerAdvice
public class IdentityResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleConstraintViolation(IllegalArgumentException e, WebRequest request) {
        log.trace(e.getLocalizedMessage(), e);

        return handleExceptionInternal(e, new ErrorResponsePayload(HttpStatus.BAD_REQUEST, e.getLocalizedMessage()).wrapper(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        log.trace(e.getLocalizedMessage(), e);

        ValidationErrorResponsePayload payload = ValidationErrorResponsePayload.builder()
            .message(DefaultResultStatus.INVALID_ARGUMENT.message())
            .status(DefaultResultStatus.INVALID_ARGUMENT.name())
            .code(DefaultResultStatus.INVALID_ARGUMENT.httpStatus().value())
            .details(e.getConstraintViolations().stream().map(v -> ValidationErrorResponsePayload.Details.builder()
                    .target(v.getPropertyPath().toString())
                    .message(v.getMessage())
                    .code(v.getMessageTemplate())
                    .build()).collect(Collectors.toList()))
            .build();

        return handleExceptionInternal(e, payload.wrapper(), new HttpHeaders(), DefaultResultStatus.INVALID_ARGUMENT.httpStatus(), request);
    }
}
