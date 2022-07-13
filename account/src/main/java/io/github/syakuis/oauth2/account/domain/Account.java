package io.github.syakuis.oauth2.account.domain;

import java.util.UUID;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface Account {
    Long getId();

    String getUsername();

    String getName();

    boolean isDisabled();

    boolean isBlocked();

    UUID getUid();

    java.time.LocalDateTime getRegisteredOn();

    java.time.LocalDateTime getUpdatedOn();
}
