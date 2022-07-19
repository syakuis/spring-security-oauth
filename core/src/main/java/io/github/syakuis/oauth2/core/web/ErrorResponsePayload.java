package io.github.syakuis.oauth2.core.web;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Getter
@EqualsAndHashCode
@ToString
public class ErrorResponsePayload implements ResponsePayload {
    private final String message;
    private final String status;
    private final int code;

    @Builder
    public ErrorResponsePayload(HttpStatus httpStatus, String message) {
        this.message = message;
        this.status = httpStatus.name();
        this.code = httpStatus.value();
    }
}
