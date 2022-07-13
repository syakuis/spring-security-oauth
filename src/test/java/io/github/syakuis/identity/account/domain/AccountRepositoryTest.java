package io.github.syakuis.oauth2.account.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-06-28
 */
@Slf4j
@Sql({"/db/account_data.h2.sql"})
@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    void findAll() {
        accountRepository.findAll();
    }

    @Test
    void findByUsername() {
        log.debug("{}", accountRepository.findByUsername("test").orElseThrow());
    }

    @Test
    void findByUid() {
    }

    @Test
    void save() {
        accountRepository.save(AccountEntity.builder()
                .name("테스터")
                .username("test")
                .password(passwordEncoder.encode("1234"))
            .build());
    }
}