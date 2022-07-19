package io.github.syakuis.oauth2.core.web;

import org.springframework.http.HttpStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface ResultStatus {
    HttpStatus httpStatus();
    String message();
    String name();
    String toString();
}
