package io.github.syakuis.oauth2.account.application.service;

import io.github.syakuis.oauth2.account.application.mapper.AccountMapper;
import io.github.syakuis.oauth2.account.application.model.AccountCommand;
import io.github.syakuis.oauth2.account.domain.Account;
import io.github.syakuis.oauth2.account.domain.AccountEntity;
import io.github.syakuis.oauth2.account.domain.AccountRepository;
import io.github.syakuis.oauth2.account.domain.AccountRepositoryCustom;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final AccountValidationService accountValidationService;

    private final AccountRepositoryCustom accountRepositoryCustom;

    public Optional<Account> account(String username) {
        Optional<AccountEntity> accountEntityOptional = accountRepository
            .findByUsername(username);

        if (accountEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(AccountMapper.INSTANCE.toDto(accountEntityOptional.get()));
    }

    public boolean usernameExists(String username) {
        return accountRepositoryCustom.existsByUsername(username);
    }

    public Account signup(AccountCommand.Signup signup) {
        accountValidationService.usernameExists(signup.getUsername());

        AccountEntity accountEntity = AccountMapper.INSTANCE.fromSignup(signup);
        accountEntity.updatePassword(signup.getPassword(), passwordEncoder::encode);

        return AccountMapper.INSTANCE.toDto(accountRepository.save(accountEntity));
    }
}
