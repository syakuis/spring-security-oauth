package io.github.syakuis.identity.account.application.service;

import io.github.syakuis.identity.account.application.enums.AccountResultStatus;
import io.github.syakuis.identity.account.application.exception.AccountResultStatusException;
import io.github.syakuis.identity.account.application.mapper.AccountMapper;
import io.github.syakuis.identity.account.application.model.AccountCommand;
import io.github.syakuis.identity.account.domain.Account;
import io.github.syakuis.identity.account.domain.AccountEntity;
import io.github.syakuis.identity.account.domain.AccountRepository;
import io.github.syakuis.identity.account.domain.AccountRepositoryCustom;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SignupAccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountRepositoryCustom accountRepositoryCustom;

    public Optional<Account> account(String username) {
        Assert.hasText(username, "username을 입력하세요.");
        Optional<AccountEntity> accountEntityOptional = accountRepository
            .findByUsername(username);

        if (accountEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(AccountMapper.INSTANCE.toDto(accountEntityOptional.get()));
    }

    public Account signup(AccountCommand.Signup signup) {
        if (accountRepositoryCustom.existsByUsername(signup.getUsername())) {
            throw new AccountResultStatusException(AccountResultStatus.EXISTS_USERNAME, "username: " + signup.getUsername());
        }

        AccountEntity accountEntity = AccountMapper.INSTANCE.fromSignup(signup);
        accountEntity.updatePassword(signup.getPassword(), passwordEncoder::encode);

        return AccountMapper.INSTANCE.toDto(accountRepository.save(accountEntity));
    }
}
