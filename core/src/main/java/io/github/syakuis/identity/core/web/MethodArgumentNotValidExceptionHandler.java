package io.github.syakuis.identity.core.web;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-13
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e, WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();

        // todo 유효성 검증 공통 구현
        ValidationErrorResponsePayload payload = ValidationErrorResponsePayload.builder()
            .message(DefaultResultStatus.INVALID_ARGUMENT.message())
            .status(DefaultResultStatus.INVALID_ARGUMENT.name())
            .code(DefaultResultStatus.INVALID_ARGUMENT.httpStatus().value())
            .details(bindingResult.getFieldErrors().stream().map(it -> ValidationErrorResponsePayload.Details.builder()
                .target(it.getField())
                .message(it.getDefaultMessage())
                .code(it.getCode())
                .build()).toList())
            .build();

        return ResponseEntity.badRequest().body(payload.wrapper());
    }
}
