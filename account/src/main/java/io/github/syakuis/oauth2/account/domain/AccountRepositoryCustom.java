package io.github.syakuis.oauth2.account.domain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-05
 */
public interface AccountRepositoryCustom {
    boolean existsByUsername(String username);
}
