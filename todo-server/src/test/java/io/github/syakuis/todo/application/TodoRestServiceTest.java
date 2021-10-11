package io.github.syakuis.todo.application;

import io.github.syakuis.todo.application.mapper.TodoDtoMapper;
import io.github.syakuis.todo.domain.TodoEntity;
import io.github.syakuis.todo.domain.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@ExtendWith(SpringExtension.class)
class TodoRestServiceTest {
    @InjectMocks
    private TodoRestService todoRestService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    void register() {
        TodoRequestDto.Register register = TodoRequestDto.Register.builder()
            .objective("테스트코드 작성하기")
            .completed(false)
            .build();

        TodoEntity entity = TodoDtoMapper.INSTANCE.toEntity(register);
        ReflectionTestUtils.setField(entity, "id", 1L);

        given(todoRepository.save(any())).willReturn(entity);

        todoRestService.register(register);
    }

}