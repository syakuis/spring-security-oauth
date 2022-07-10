package io.github.syakuis.identity.account.application.service;

import io.github.syakuis.identity.account.application.enums.AccountResultStatus;
import io.github.syakuis.identity.account.application.exception.AccountResultStatusException;
import io.github.syakuis.identity.account.application.mapper.AccountMapper;
import io.github.syakuis.identity.account.domain.Account;
import io.github.syakuis.identity.account.domain.AccountEntity;
import io.github.syakuis.identity.account.domain.AccountRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-06-28
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PasswordAccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public boolean matches(UUID uid, String password) {
        Assert.notNull(uid, "uid must not be null");
        Assert.hasText(password, "password must not be empty");

        return passwordEncoder.matches(password, accountRepository.findByUid(uid).orElseThrow(() -> {
            throw new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "uid: " + uid);
        }).getPassword());
    }

    public Account change(UUID uid, String password) {
        Assert.notNull(uid, "uid must not be null");
        Assert.hasText(password, "password must not be empty");

        AccountEntity accountEntity = accountRepository.findByUid(uid).orElseThrow(() -> {
            throw new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "uid: " + uid);
        });

        accountEntity.updatePassword(passwordEncoder.encode(password));

        return AccountMapper.INSTANCE.toDto(accountEntity);
    }
}
