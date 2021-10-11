package io.github.syakuis.todo.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/todo/v1/todos")
public class TodoRestController {
    private final TodoRestService todoRestService;

    @PostMapping
    public ResponseEntity<TodoResponseDto.Body> register(@RequestBody TodoRequestDto.Register register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoRestService.register(register));
    }
}
