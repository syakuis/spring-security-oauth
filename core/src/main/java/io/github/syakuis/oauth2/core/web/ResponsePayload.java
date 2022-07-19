package io.github.syakuis.oauth2.core.web;

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

    default Map<String, ResponsePayload> wrapper() {
        return JsonRootName.of("error", this);
    }
}
