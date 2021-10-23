package io.github.syakuis.todo.application;

import io.github.syakuis.todo.application.mapper.TodoDtoMapper;
import io.github.syakuis.todo.domain.TodoEntity;
import io.github.syakuis.todo.domain.TodoRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TodoRestService {
    private final TodoRepository todoRepository;

    public TodoResponseDto.Body register(TodoRequestDto.Register register) {
        TodoEntity todo = TodoDtoMapper.INSTANCE.toEntity(register);
        todoRepository.save(todo);

        return TodoDtoMapper.INSTANCE.toDTO(todo);
    }
}
