package io.github.syakuis.oauth2.account.domain;

import io.github.syakuis.oauth2.account.application.enums.AccountResultStatus;
import io.github.syakuis.oauth2.account.application.exception.AccountResultStatusException;
import io.github.syakuis.oauth2.account.application.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-06-28
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountDomainService {
    private final AccountRepository accountRepository;

    public Account account(String username) {
        AccountEntity accountEntity = accountRepository.findByUsername(username).orElseThrow(() -> {
            throw new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "username: " + username);
        });

        return AccountMapper.INSTANCE.toDto(accountEntity);
    }

}
