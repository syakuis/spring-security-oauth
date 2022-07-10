package io.github.syakuis.identity.core.web;

import java.util.Map;
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

    public ErrorResponsePayload(HttpStatus httpStatus, String message) {
        this.message = message;
        this.status = httpStatus.name();
        this.code = httpStatus.value();
    }

    @Builder
    public ErrorResponsePayload(String message, String status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    @Override
    public Map<String, ResponsePayload> wrapper() {
        return JsonRootName.of("error", this);
    }
}
