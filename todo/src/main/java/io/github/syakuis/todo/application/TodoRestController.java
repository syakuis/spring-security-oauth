package io.github.syakuis.todo.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/todo/v1/todos")
class TodoRestController {
    private final TodoRestService todoRestService;

    @PostMapping
    ResponseEntity<TodoResponseDto.Body> register(@RequestBody TodoRequestDto.Register register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoRestService.register(register));
    }

    @GetMapping("/{id}")
    ResponseEntity<TodoResponseDto.Body> todo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(TodoResponseDto.Body.builder().build());
    }
}
