package io.github.syakuis.todo.application;

import lombok.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoRequestDto {

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class Register {
        String objective;
        boolean completed;
    }
}
