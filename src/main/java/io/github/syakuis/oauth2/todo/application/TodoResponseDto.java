package io.github.syakuis.oauth2.todo.application;

import io.github.syakuis.oauth2.todo.domain.Todo;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

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
