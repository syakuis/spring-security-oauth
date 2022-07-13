package io.github.syakuis.oauth2.account.application.service;

import io.github.syakuis.oauth2.account.application.enums.AccountResultStatus;
import io.github.syakuis.oauth2.account.application.exception.AccountResultStatusException;
import io.github.syakuis.oauth2.account.application.mapper.AccountMapper;
import io.github.syakuis.oauth2.account.application.model.AccountCommand;
import io.github.syakuis.oauth2.account.domain.Account;
import io.github.syakuis.oauth2.account.domain.AccountEntity;
import io.github.syakuis.oauth2.account.domain.AccountRepository;
import io.github.syakuis.oauth2.account.domain.AccountRepositoryCustom;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-11
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ManagerAccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountRepositoryCustom accountRepositoryCustom;

    public boolean isExistsUsername(String username) {
        return accountRepositoryCustom.existsByUsername(username);
    }
    public List<? extends Account> accounts() {
        return accountRepository.findAll().stream().map(AccountMapper.INSTANCE::toDto).toList();
    }

    public Account account(UUID uid) {
        Assert.notNull(uid, "uid must not be null");
        AccountEntity accountEntity = accountRepository.findByUid(uid).orElseThrow(() -> {
            throw new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "uid: " + uid);
        });
        return AccountMapper.INSTANCE.toDto(accountEntity);
    }

    public Account update(UUID uid, AccountCommand.Update update) {
        AccountEntity accountEntity = accountRepository.findByUid(uid)
            .orElseThrow(() -> new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "uid: " + uid));

        accountEntity.updatePassword(passwordEncoder.encode(update.getPassword()));

        AccountEntity source = AccountMapper.INSTANCE.fromUpdate(update);
        accountEntity.update(source);

        return AccountMapper.INSTANCE.toDto(accountEntity);
    }
}
