package io.github.syakuis.core.test.restdocs.constraints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
@RequiredArgsConstructor
@Getter
public class SingleDescriptor implements Descriptor {
    @Accessors(fluent = true)
    private final String name;
    private final String description;
    private final boolean optional;
}
