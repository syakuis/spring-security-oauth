package io.github.syakuis.oauth2.core.web;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-02
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
final class JsonRootName {
    public static <T> Map<String, T> of(String name, T object) {
        return Map.of(name, object);
    }
}
