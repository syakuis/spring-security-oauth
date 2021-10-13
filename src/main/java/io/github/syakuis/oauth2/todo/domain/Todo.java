package io.github.syakuis.oauth2.todo.domain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
public interface Todo {
    Long getId();

    String getObjective();

    boolean isCompleted();

    java.time.LocalDateTime getRegisteredOn();

    java.time.LocalDateTime getCompletedOn();
}
