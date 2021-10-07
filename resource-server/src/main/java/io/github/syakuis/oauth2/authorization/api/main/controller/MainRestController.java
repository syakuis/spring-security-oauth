package io.github.syakuis.oauth2.authorization.api.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
public class MainRestController {
    @GetMapping
    public ResponseEntity<String> go() {
        return ResponseEntity.ok("ok");
    }
}
