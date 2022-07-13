package io.github.syakuis.oauth2.restdocs;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
public interface FieldSpec {
    String name();
    String getDescription();
    boolean isOptional();
}
