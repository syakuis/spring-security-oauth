package io.github.syakuis.authorization.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PasswordEncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void test() {
        log.debug(passwordEncoder.encode("1234"));
    }
}
