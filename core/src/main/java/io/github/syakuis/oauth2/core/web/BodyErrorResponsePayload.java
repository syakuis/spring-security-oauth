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
@EqualsAndHashCode(callSuper = true)
@ToString
public class BodyErrorResponsePayload<T> extends ErrorResponsePayload {
    private final T data;

    @Builder(builderMethodName = "bodyBuilder")
    public BodyErrorResponsePayload(HttpStatus httpStatus, String message, T data) {
        super(httpStatus, message);
        this.data = data;
    }
}
