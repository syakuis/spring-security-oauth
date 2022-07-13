package io.github.syakuis.identity.core.web;

import java.util.Map;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface ResponsePayload {
    String getMessage();
    /**
     * [a-zA-Z0-9_] naming...
     * @return
     */
    String getStatus();
    int getCode();

    Map<String, ResponsePayload> wrapper();
}
