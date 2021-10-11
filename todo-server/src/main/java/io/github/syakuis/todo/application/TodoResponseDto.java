package io.github.syakuis.todo.application;

import io.github.syakuis.todo.domain.Todo;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoResponseDto {

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class Body implements Todo {
        Long id;
        String objective;
        boolean completed;
        LocalDateTime registeredOn;
        LocalDateTime completedOn;
    }
}
