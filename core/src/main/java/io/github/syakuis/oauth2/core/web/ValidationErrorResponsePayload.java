package io.github.syakuis.oauth2.core.web;

import java.util.List;
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
@EqualsAndHashCode(callSuper = true)
@ToString
public class ValidationErrorResponsePayload extends ErrorResponsePayload {
    private final List<Details> details;

    @Builder(builderMethodName = "validBuilder")
    public ValidationErrorResponsePayload(HttpStatus httpStatus, String message,
        List<Details> details) {
        super(httpStatus, message);
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
}
