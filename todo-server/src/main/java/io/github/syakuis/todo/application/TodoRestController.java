package io.github.syakuis.todo.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@RestController
@RequestMapping("/todo/v1/todos")
public class TodoRestController {
    @PostMapping
    public ResponseEntity<TodoResponseDto.Body> register(@RequestBody TodoRequestDto.Register register) {
        return ResponseEntity.ok().build();
    }
}
