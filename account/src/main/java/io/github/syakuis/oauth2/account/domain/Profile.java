package io.github.syakuis.oauth2.account.domain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
public interface Profile {
    String getUsername();

    String getName();

    java.time.LocalDateTime getRegisteredOn();

    java.time.LocalDateTime getUpdatedOn();

    java.util.UUID getUid();
}
