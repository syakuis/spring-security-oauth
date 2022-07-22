package io.github.syakuis.oauth2.authorizationserver.application.restdocs;

import io.github.syakuis.core.test.restdocs.constraints.Descriptor;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */

@Getter
public enum JwtKeysField implements Descriptor {
    keys("keys", false),
    kty("kty", false),
    e("e", false),
    use("use", false),
    alg("alg", false),
    n("n", false)
    ;

    private final String description;
    private final boolean optional;

    JwtKeysField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }

    public static Descriptor[] responseKeys() {
        return new Descriptor[] {
            kty,
            e,
            use,
            alg,
            n
        };
    }
}
