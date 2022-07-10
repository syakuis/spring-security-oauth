package io.github.syakuis.identity.core.web;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Getter
@EqualsAndHashCode
@ToString
public class ValidationErrorResponsePayload implements ResponsePayload {
    private final String message;
    private final String status;
    private final int code;
    private final List<Details> details;

    public ValidationErrorResponsePayload(HttpStatus httpStatus, String message,  List<Details> details) {
        this.message = message;
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.details = details;
    }

    @Builder
    public ValidationErrorResponsePayload(String message, String status, int code, List<Details> details) {
        this.message = message;
        this.status = status;
        this.code = code;
        this.details = details;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Details {
        private String target;
        private String message;
        private String code;
    }

    @Override
    public Map<String, ResponsePayload> wrapper() {
        return JsonRootName.of("error", this);
    }
}
