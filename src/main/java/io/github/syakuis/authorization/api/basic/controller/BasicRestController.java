package io.github.syakuis.authorization.api.basic.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequestMapping("/api/basic")
public class BasicRestController {

    @GetMapping
    public ResponseEntity<String> basic() {
        return ResponseEntity.ok("ok");
    }
}
