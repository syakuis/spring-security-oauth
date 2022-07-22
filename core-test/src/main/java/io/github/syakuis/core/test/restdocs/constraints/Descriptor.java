package io.github.syakuis.core.test.restdocs.constraints;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
public interface Descriptor {
    String name();
    String getDescription();
    boolean isOptional();
}
