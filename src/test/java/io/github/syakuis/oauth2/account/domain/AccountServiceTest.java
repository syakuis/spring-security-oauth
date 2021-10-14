package io.github.syakuis.oauth2.account.domain;

import io.github.syakuis.oauth2.configuration.TestProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-14
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class AccountServiceTest {
    @Autowired
    private TestProperties props;

    @Autowired
    private AccountService accountService;

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void findByUsername() {
        Account account = accountService.findByUsername(props.getUsername());
        assertNotNull(account);
        assertEquals(account.getUsername(), props.getUsername());
    }
}