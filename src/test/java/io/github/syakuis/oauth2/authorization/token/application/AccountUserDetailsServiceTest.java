package io.github.syakuis.oauth2.authorization.token.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        UserDetails userDetails = accountUserDetailsService.loadUserByUsername("test");
        assertNotNull(userDetails);
        assertEquals("test", userDetails.getUsername());
    }
}