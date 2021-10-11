package io.github.syakuis.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}
