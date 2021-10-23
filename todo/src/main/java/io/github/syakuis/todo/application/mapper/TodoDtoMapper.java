package io.github.syakuis.todo.application.mapper;

import io.github.syakuis.todo.application.TodoRequestDto;
import io.github.syakuis.todo.application.TodoResponseDto;
import io.github.syakuis.todo.domain.Todo;
import io.github.syakuis.todo.domain.TodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Mapper
public interface TodoDtoMapper {
    TodoDtoMapper INSTANCE = Mappers.getMapper(TodoDtoMapper.class);

    TodoResponseDto.Body toDTO(Todo todo);

    @Mapping(target = "registeredOn", ignore = true)
    @Mapping(target = "completedOn", ignore = true)
    TodoEntity toEntity(TodoRequestDto.Register register);
}
