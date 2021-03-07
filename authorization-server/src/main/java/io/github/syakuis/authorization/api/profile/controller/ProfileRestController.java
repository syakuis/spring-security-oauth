package io.github.syakuis.authorization.api.profile.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Profile("test")
@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {

    @GetMapping
    public ResponseEntity<String> profile(OAuth2Authentication authentication) {
        log.debug("{}", authentication);
        return ResponseEntity.ok("ok");
    }
}
