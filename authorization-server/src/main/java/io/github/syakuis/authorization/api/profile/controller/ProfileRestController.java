package io.github.syakuis.authorization.api.profile.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {

    @GetMapping
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("ok");
    }
}
