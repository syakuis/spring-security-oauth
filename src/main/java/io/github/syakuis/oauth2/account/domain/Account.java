package io.github.syakuis.oauth2.account.domain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
public interface Account {
    Long getId();

    String getUsername();

    String getName();

    java.util.UUID getUid();

    String getPassword();

    Boolean getDisabled();

    Boolean getBlocked();

    boolean isDeleted();

    java.time.LocalDateTime getRegisteredOn();

    java.time.LocalDateTime getUpdatedOn();
}
