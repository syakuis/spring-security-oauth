package io.github.syakuis.oauth2.authorization.token.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-12
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountUserDetailsServiceTest {
    @Autowired
    private AccountUserDetailsService accountUserDetailsService;

    @Test
    void loadUserByUsername() {
        accountUserDetailsService.loadUserByUsername("test");
    }
}